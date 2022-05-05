package cn.guankejian.test.bean

import com.google.gson.annotations.SerializedName

data class BaseResponse<T : Any>(
    @SerializedName("data") val item: T
)