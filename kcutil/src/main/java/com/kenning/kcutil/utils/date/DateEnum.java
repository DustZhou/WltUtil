package com.kenning.kcutil.utils.date;

/**
 * 日期枚举
 * @author 陈少华
 * @date 2017/12/13
 */

public enum DateEnum {
    /**昨日 0 */
    YESTERDAY,
    /**今日 1 */
    TODAY,
    /**7天前 2 */
    WEEK,
    /**一年前 3 */
    YEAR,
    /**1月前 4 */
    MONTH,
    /**30天前 5 */
    THIRTY_DAYS,
    /**3个月前 6 */
    THREE_MONTHS,
    /**近半年 7 */
    HALF_YEAR,
    /**本月初 8 */
    THISMONTH,
    /**本季度初 9 */
    THISQUARTER,
    /**未来7天 10 */
    Next7Days,
    /**未来30天 11 */
    Next30Days,
    /**其他*/
    OTHER;

    public static DateEnum indexOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
