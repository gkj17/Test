package cn.guankejian.test.bean

sealed class TagUIModel {
    data class Item(val item:MZTag):TagUIModel()
    data class End(val item:String):TagUIModel()
    data class Sep(val item:String):TagUIModel()
}