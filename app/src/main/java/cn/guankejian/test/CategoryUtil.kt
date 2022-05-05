package cn.guankejian.test


object CategoryUtil {
    const val IC_NAME_SETTING = "Setting"

    const val IC_OTHER = "Other"

    const val IC_YI = "Clothes"

    const val IC_SHI = "Food"

    const val IC_ZHU = "Live"

    const val IC_XING = "Walk"

    /**   */
    const val IC_SHE_JIAO = "社交"

    /**   */
    const val IC_TONG_XUN = "通讯"

    /**   */
    const val IC_RI_YONG_PIN = "日用品"

    /**   */
    const val IC_YI_LIAO_JIAN_KANG = "医疗健康"

    /**   */
    const val IC_HU_FU_HUA_ZHUANG = "护肤化妆"


    /**   */
    const val IC_GONG_ZI = "Salary"

    /**   */
    const val IC_JIANG_JIN = "奖金"

    /**   */
    const val IC_LI_CAI = "理财"

    /**   */
    const val IC_HONG_BAO = "Red Packet"

    /**   */
    const val IC_ZHONG_ZHUAN = "中转"


    /** 所有分类图标  */
    val ALL_ICON = listOf(
        IC_OTHER,
        IC_YI,
        IC_SHI,
        IC_ZHU,
        IC_XING,
        IC_SHE_JIAO,
        IC_TONG_XUN,
        IC_RI_YONG_PIN,
        IC_YI_LIAO_JIAN_KANG,
        IC_HU_FU_HUA_ZHUANG,
        IC_GONG_ZI,
        IC_JIANG_JIN,
        IC_LI_CAI,
        IC_HONG_BAO,
        IC_ZHONG_ZHUAN
    )


    fun resId(iconName: String?): Int {
        return if (iconName.isNullOrBlank()) {
            R.drawable.ic_category_expense_other
        } else when (iconName) {
            IC_NAME_SETTING -> R.drawable.ic_setting_category
            IC_OTHER -> R.drawable.ic_category_expense_other
            IC_YI -> R.drawable.ic_category_expense_clothes
            IC_SHI -> R.drawable.ic_category_expense_food
            IC_ZHU -> R.drawable.ic_category_expense_house
            IC_XING -> R.drawable.ic_category_expense_traffic
            IC_SHE_JIAO -> R.drawable.ic_category_expense_favor_pattern
            IC_TONG_XUN -> R.drawable.ic_category_expense_communication
            IC_RI_YONG_PIN -> R.drawable.ic_category_expense_daily_necessities
            IC_YI_LIAO_JIAN_KANG -> R.drawable.ic_category_expense_medical
            IC_HU_FU_HUA_ZHUANG -> R.drawable.ic_category_expense_huazhuang

            IC_GONG_ZI -> R.drawable.ic_category_income_salary
            IC_JIANG_JIN -> R.drawable.ic_category_income_reward

            IC_LI_CAI -> R.drawable.ic_category_income_invest_profit
            IC_HONG_BAO -> R.drawable.ic_category_expense_favor_pattern
            IC_ZHONG_ZHUAN -> R.drawable.ic_category_income_invest_recovery

            else -> R.drawable.ic_category_expense_other
        }
    }
}