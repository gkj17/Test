package cn.guankejian.test.bean

import com.google.gson.annotations.SerializedName

data class BaseListResponse<T : Any>(
    @SerializedName("data") val items: List<T> = emptyList()
)