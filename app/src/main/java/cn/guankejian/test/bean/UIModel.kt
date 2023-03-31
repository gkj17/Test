package cn.guankejian.test.bean

sealed class UIModel{
    data class IconItem(val item: MZTag):UIModel()
    data class HeaderItem(val item: String):UIModel()
    data class SepItem(val item:String):UIModel()
    data class FooterItem(val item:String):UIModel()
}