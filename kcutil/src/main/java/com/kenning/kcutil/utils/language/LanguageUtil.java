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

import com.kenning.kcutil.R;

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
            return c.getString(resID);
        } else {
            return chineseText; // 如果没有找到对应的资源 ID，返回原始中文文本
        }
    }


    /**
     * 如果是7.0以下，我们需要调用changeAppLanguage方法，
     * 如果是7.0及以上系统，直接把我们想要切换的语言类型保存在SharedPreferences中即可
     * 然后重新启动MainActivity
     *
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
    private static Locale getLocaleByLanguage(String language) {
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        switch (language) {
            case "ch":
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case "en":
                locale = Locale.ENGLISH;
                break;
            case "vi":
                locale = new Locale("vi");
                break;
            case "zh-Hant":
                locale = Locale.TRADITIONAL_CHINESE;
                break;
            case "ar":
                locale = new Locale("ar");
                break;
            case "bg":
                locale = new Locale("bg");
                break;
            case "ca":
                locale = new Locale("ca");
                break;
            case "cs":
                locale = new Locale("cs");
                break;
            case "da":
                locale = new Locale("da");
                break;
            case "de":
                locale = new Locale("de");
                break;
            case "el":
                locale = new Locale("el");
                break;
            case "es":
                locale = new Locale("es");
                break;
            case "fi":
                locale = new Locale("fi");
                break;
            case "fr":
                locale = new Locale("fr");
                break;
            case "iw":
                locale = new Locale("iw");
                break;
            case "hi":
                locale = new Locale("hi");
                break;
            case "hr":
                locale = new Locale("hr");
                break;
            case "hu":
                locale = new Locale("hu");
                break;
            case "in":
                locale = new Locale("in");
                break;
            case "it":
                locale = new Locale("it");
                break;
            case "ja":
                locale = new Locale("ja");
                break;
            case "ko":
                locale = new Locale("ko");
                break;
            case "lt":
                locale = new Locale("lt");
                break;
            case "lv":
                locale = new Locale("lv");
                break;
            case "nb":
                locale = new Locale("nb");
                break;
            case "nl":
                locale = new Locale("nl");
                break;
            case "pl":
                locale = new Locale("pl");
                break;
            case "pt":
                locale = new Locale("pt");
                break;
            case "ro":
                locale = new Locale("ro");
                break;
            case "ru":
                locale = new Locale("ru");
                break;
            case "sk":
                locale = new Locale("sk");
                break;
            case "sl":
                locale = new Locale("sl");
                break;
            case "sr":
                locale = new Locale("sr");
                break;
            case "sv":
                locale = new Locale("sv");
                break;
            case "th":
                locale = new Locale("th");
                break;
            case "tl":
                locale = new Locale("tl");
                break;
            case "tr":
                locale = new Locale("tr");
                break;
            case "uk":
                locale = new Locale("uk");
                break;
            default:
                locale = Locale.ENGLISH;
                break;
        }
        return locale;
    }

    /**
     * @Description:用于基类中设置语言。如果没有基类的弹窗，可以调用此方法
     * @author: create by zyl on 2025/01/09
     */
    public static Context attachBaseContext(Context context) {
        String language = getCurrentLanguage(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }
    /**
    * @Description:获取当前语言
    * @author: create by zyl on 2025/01/11
    */
    public static String getCurrentLanguage(Context context) {
        String getType = "lng";
        String getKey = "language";
        //获取当前环境的Resources
        Resources resources = context.getResources();
        //获得res资源对象
        Configuration config = resources.getConfiguration();
        //  获得屏幕参数：主要是分辨率，像素等。
        String mLanguage = config.locale.getLanguage();
        String mCountry = config.locale.getCountry();
        if (config.locale.getLanguage().equals("zh")) {
            mLanguage = LanguageType.CHINESE.getLanguage();

            if (!mCountry.equals("CN")) {
                mLanguage = LanguageType.CHINESE.getLanguage();
                // 暂不支持繁体中文，后期要判断地区。非大陆地区用繁体中文
                // mLanguage = Locale.TRADITIONAL_CHINESE.getLanguage();
            }
        } else {
            mLanguage = "en";
        }
        SharedPreferences preferences = context.getSharedPreferences(getType,
                Activity.MODE_PRIVATE);
        String language = preferences.getString(getKey, mLanguage);
        return language;
    }

    /**
     * @Description:设置当前语言
     * @author: create by zyl on 2025/01/11
     */
    public static void setCurrentLanguage(Context context,String newLanguage) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "lng",
                Context.MODE_PRIVATE
        ).edit();
        editor.putString("language", newLanguage);
        editor.commit();
    }

    /**
     * @Description: 更改Locale
     * @author: create by zyl on 2025/01/09
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = getLocaleByLanguage(language);

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    /**
     * @Description:多语言类型。文字、图片、网页的文件夹后缀(String language)命名
     * * @author: create by zyl on 2025/01/10
     */
    public enum LanguageType {

        /**
         * * @Description:简体中文（文件夹不需要后缀，默认是简体中文）
         */
        CHINESE("ch"),
        /**
         * @Description:繁体中文
         */
        TRADITIONAL_CHINESE("zh-Hant"),
        /**
         * @Description:英文
         */
        ENGLISH("en"),
        /**
         * @Description:阿拉伯文
         */
        ARABIC("ar"),
        /**
         * @Description:保加利亚文
         */
        BULGARIAN("bg"),
        /**
         * @Description:加泰罗尼亚文
         */
        CATALAN("ca"),
        /**
         * @Description:捷克文
         */
        CZECK("cs"),
        /**
         * @Description:丹麦文
         */
        DANISH("da"),
        /**
         * @Description:德文
         */
        GERMAN("de"),
        /**
         * @Description:希腊文
         */
        GREEK("el"),
        /**
         * @Description:西班牙文
         */
        SPANISH("es"),
        /**
         * @Description:芬兰文
         */
        FINNISH("fi"),
        /**
         * @Description:法文
         */
        FRENCH("fr"),
        /**
         * @Description:希伯来文
         */
        HEBREW("iw"),
        /**
         * @Description:印地文
         */
        HINDI("hi"),
        /**
         * @Description:克罗里亚文
         */
        CROATIAN("hr"),
        /**
         * @Description:匈牙利文
         */
        HUNGARIAN("hu"),
        /**
         * @Description:印度尼西亚文
         */
        INDONESIAN("in"),
        /**
         * @Description:意大利文
         */
        ITALIAN("it"),
        /**
         * @Description:日文
         */
        JAPANESE("ja"),
        /**
         * @Description:韩文
         */
        KOREAN("ko"),
        /**
         * @Description:立陶宛文
         */
        LITHUANIAN("lt"),
        /**
         * @Description:拉脱维亚文
         */
        LATVIAN("lv"),
        /**
         * @Description:挪威博克马尔文
         */
        NORWEGIAN_BOKMAL("nb"),
        /**
         * @Description:荷兰文
         */
        DUTCH("nl"),
        /**
         * @Description:波兰文
         */
        POLISH("pl"),
        /**
         * @Description:葡萄牙文
         */
        PORTUGUESE("pt"),
        /**
         * @Description:罗马尼亚文
         */
        ROMANIAN("ro"),
        /**
         * @Description:俄文
         */
        RUSSIAN("ru"),
        /**
         * @Description:斯洛伐克文
         */
        SLOVAK("sk"),
        /**
         * @Description:斯洛文尼亚文
         */
        SLOVENIAN("sl"),
        /**
         * @Description:塞尔维亚文
         */
        SERBIAN("sr"),
        /**
         * @Description:瑞典文
         */
        SWEDISH("sv"),
        /**
         * @Description:泰文
         */
        THAI("th"),
        /**
         * @Description:塔加洛语
         */
        TAGALOG("tl"),
        /**
         * @Description:土耳其文
         */
        TURKISH("tr"),
        /**
         * @Description:乌克兰文
         */
        UKRAINIAN("uk"),
        /**
         * @Description:越南文
         */
        VIETNAMESE("vi");


        private String language;

        LanguageType(String language) {
            this.language = language;
        }

        public String getLanguage() {
            return language == null ? "" : language;
        }
    }
}

