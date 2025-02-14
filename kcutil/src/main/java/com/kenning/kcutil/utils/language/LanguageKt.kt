package com.kenning.kcutil.utils.language

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.loader.ResourcesLoader
import android.content.res.loader.ResourcesProvider
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.util.Locale


object ResourceHelper {
    fun loadLanguage(context: Context, langCode: String) {
        try {
            // 1. 创建模拟的 resources 目录结构
            val resDir = File(context.filesDir, "dynamic_res")
            val valuesDir = File(resDir, "values-$langCode")
            if (!valuesDir.exists()) valuesDir.mkdirs()

            // 2. 将下载的 strings.xml 复制到对应目录
            val stringsFile = File(valuesDir, "strings.xml")
            // 假设已从服务器下载并保存到此处

            // 3. 通过反射将目录添加到 AssetManager
            val assetManager = context.assets
            val addAssetPath = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPath.isAccessible = true
            val cookie = addAssetPath.invoke(assetManager, resDir.absolutePath) as Int

            // 4. 更新 Configuration 并重启 Activity
            val resources = context.resources
            val config = Configuration(resources.configuration)
            config.locale = Locale(langCode)
            resources.updateConfiguration(config, resources.displayMetrics)

            // 5. 重启 Activity 使新资源生效
            if (context is Activity) {
                context.recreate()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
/**
 * @Description:
 * @author: create by zyl on 2025-02-13
 */
object LanguageKt {
    /**
    * @Description:Android 11+ 使用 ResourcesProvider（推荐）
    * @author: create by zyl on 2025/02/13
    */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun loadLanguageForAndroid11(context: Context, langCode: String) {
        // 1. 准备动态资源文件（假设已下载到 files/dynamic_res/values-zh/strings.xml）
        val resDir = File(context.filesDir, "dynamic_res")

        // 2. 创建 ResourcesProvider
        val resourcesProvider = ResourcesProvider.loadFromDirectory(
            resDir.absolutePath,
            null // 可传递 AssetsProvider 处理更复杂逻辑
        )

        // 3. 将 Provider 添加到全局 ResourcesLoader
        val resources = context.resources
        val loader = ResourcesLoader()
        loader.addProvider(resourcesProvider)
        resources.addLoaders(loader) // 动态资源生效

        // 4. 更新 Configuration
        val config = Configuration(resources.configuration)
        config.setLocale(Locale(langCode))
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * @Description:Android 5.0 - 10 使用反射（需适配）
     * @author: create by zyl on 2025/02/13
     */
    private fun loadLanguageLegacy(context: Context, langCode: String) {
        try {
            // 1. 准备资源目录（同上）
            val resDir = File(context.filesDir, "dynamic_res")

            // 2. 反射获取 AssetManager
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(assetManager, resDir.absolutePath)

            // 3. 替换应用的 Resources
            val newResources = Resources(
                assetManager,
                context.resources.displayMetrics,
                context.resources.configuration
            )
            val appContext = context.applicationContext
            val field = appContext.javaClass.getDeclaredField("mResources")
            field.isAccessible = true
            field.set(appContext, newResources)

            // 4. 更新 Configuration
            val config = Configuration(newResources.configuration)
            config.setLocale(Locale(langCode))
            newResources.updateConfiguration(config, newResources.displayMetrics)
        } catch (e: Exception) {
            // 反射在 Android 9+ 可能失败，需降级处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Android 9+ 不允许直接反射 AssetManager，提示用户重启应用
                Toast.makeText(context, "Please restart the app", Toast.LENGTH_SHORT).show()
            } else {
                e.printStackTrace()
            }
        }
    }

    /**
     * @Description:统一入口方法
     * @author: create by zyl on 2025/02/13
     */
    fun switchLanguage(context: Context, langCode: String) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                // Android 11+ 使用官方 API
                loadLanguageForAndroid11(context, langCode)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                // Android 5.0 - 10 使用反射方案
                loadLanguageLegacy(context, langCode)
            }

            else -> {
                // API <21 提示需要重启应用
                Toast.makeText(context, "Restart app to apply language", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * @Description:在下载语言包后，校验文件完整性：
     * @author: create by zyl on 2025/02/13
     */

    fun validateStringsXml(file: File): Boolean {
        return try {
            // 解析 XML 检查格式是否正确
            XmlPullParserFactory.newInstance().newPullParser().apply {
                setInput(FileInputStream(file), "UTF-8")
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    next()
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * @Description:若反射在部分设备上不可用，提供“重启生效”的友好提示：
     * @author: create by zyl on 2025/02/13
     */
    fun showRestartDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Language Changed")
            .setMessage("Restart app to apply the new language?")
            .setPositiveButton("Restart") { _, _ ->
                // 杀死进程并重启
                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
                Runtime.getRuntime().exit(0)
            }
            .setNegativeButton("Later", null)
            .show()
    }
}