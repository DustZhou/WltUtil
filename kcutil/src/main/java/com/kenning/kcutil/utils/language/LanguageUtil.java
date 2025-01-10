package com.kenning.kcutil.utils.language;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kenning.kcutil.R;
import com.kenning.kcutil.utils.dialog.easydialog.DialogTools;
import com.zyyoona7.popup.AdaptionDialog;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * 语言切换
 */
public class LanguageUtil {
    private static final String TAG = "LanguageUtil";
    /**
     * @Description:权限和单据类型是读数据包的是中文，通过中文键去映射集合里去找并最终显示对应语言的字符串
     * @author: create by zyl on 2025/01/09
     */
    private static final Map<String, Integer> chineseToResourceIdMap = new HashMap<>();
    /**
     * @Description:权限和单据类型是读数据包的是中文，通过中文键去映射集合里去找并最终显示对应语言的字符串
     * @author: create by zyl on 2025/01/09
     */
    static {
        chineseToResourceIdMap.put("切换", R.string.切换);
        //todo 添加其他映射。需要补齐，主要用于单据类型和权限
    }
    /**
    * @Description:权限和单据类型是读数据包的是中文，通过中文键去映射集合里去找并最终显示对应语言的字符串
    * @author: create by zyl on 2025/01/09
    */
    public static String getLocalizedTextFromChinese(Context c, String chineseText) {
        Integer resID = chineseToResourceIdMap.get(chineseText);
        if (resID != null) {
            return getLngString(c, resID);
        } else {
            return chineseText; // 如果没有找到对应的资源 ID，返回原始中文文本
        }
    }


    /**
     * 如果是7.0以下，我们需要调用changeAppLanguage方法，
     * 如果是7.0及以上系统，直接把我们想要切换的语言类型保存在SharedPreferences中即可
     * 然后重新启动MainActivity
     * @param newLanguage 想要切换的语言类型 比如 "en" ,"zh"
     */
    public static void changeAppLanguage(Context context, String newLanguage) {
        if (TextUtils.isEmpty(newLanguage)) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            //获取想要切换的语言类型
            Locale locale = getLocaleByLanguage(newLanguage);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            } else {
                configuration.locale = locale;
            }
            //更新配置
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "lng",
                Context.MODE_PRIVATE
        ).edit();
        editor.putString("language", newLanguage);
        editor.commit();


    }
    /**
    * @Description:通过语言类型获取Locale对象
    * @author: create by zyl on 2025/01/09
    */
    public static Locale getLocaleByLanguage(String language) {
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        if (language.equals(LanguageType.CHINESE.getLanguage())) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals(LanguageType.ENGLISH.getLanguage())) {
            locale = Locale.ENGLISH;
        }
        Log.d(TAG, "getLocaleByLanguage: " + locale.getDisplayName());
        return locale;
    }

    /**
    * @Description:用于基类中设置语言。如果没有基类的弹窗，可以调用此方法
    * @author: create by zyl on 2025/01/09
    */
    public static Context attachBaseContext(Context context) {
        String getType = "lng";
        String getKey = "language";
        String defValue = "ch";
        SharedPreferences preferences = context.getSharedPreferences(getType,
                Activity.MODE_PRIVATE);
        String language = preferences.getString(getKey, defValue);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }
    /**
    * @Description: 更改Locale
    * @author: create by zyl on 2025/01/09
    */
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = LanguageUtil.getLocaleByLanguage(language);

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    /**
    * @Description:通过资源ID获取对应语言的字符串
    * @author: create by zyl on 2025/01/09
    */
    public static String getLngString(Context c, int resID) {
        String lng = new DialogTools().getPreferences(c, "lng", "language", "ch");
        String name;
        //获取当前环境的Resources
        Resources resources = c.getResources();
        //获得res资源对象
        Configuration config = resources.getConfiguration();
        Locale oldLocale = config.locale;
        //获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();
        //  获得屏幕参数：主要是分辨率，像素等。
        config.locale = getLocaleByLanguage(lng);
        // 英文
        resources.updateConfiguration(config, dm);
        name = resources.getString(resID);
        return name;
    }

    /**
     * 软件语言是否为中文
     *
     * @param c
     * @return
     */
    public static Boolean isChinese(Context c) {
        return new DialogTools().getPreferences(c, "lng", "language", "ch").equals("ch");
    }


    enum LanguageType {

        CHINESE("ch"),
        ENGLISH("en-rUS");

        private String language;

        LanguageType(String language) {
            this.language = language;
        }

        public String getLanguage() {
            return language == null ? "" : language;
        }
    }
}

