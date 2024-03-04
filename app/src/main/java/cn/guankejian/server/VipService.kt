package cn.guankejian.server
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface VipService{


    @GET("x/api/hot_search")
    suspend fun index(
    ):TestData

    @GET("vip/index")
    suspend fun index2(
    ):BaseListResponse<VipIndexList>


}

data class TestData(
    val businessHead:TestData2,
    val ret:Int
)

data class TestData2(
    val type:Int,
    val body:String
)
data class BaseListResponse<T : Any>(
    @SerializedName("data") val items: List<T> = emptyList()
)


data class VipIndexList(
    val mapResult:List<VipIndexDetail>
)

data class VipIndexDetail(
    val name:String,
    val items:List<VipIndexItem>
)

data class VipIndexItem(
    val img: String,
    var title: String,
    var link: String,
    var tip: String,
)


data class PlayInfos(
    val type:String,
    val data:List<PlayInfo>
)
data class PlayInfo(
    val name:String,
    val data:List<PlayItem>
)

data class PlayItem(
    var name:String,
    val url:String,
)

data class VideoInfo(
    val type:String,
    var url:String
){
    fun isVideo():Boolean{
        return type == "m3u8" || type == "mp4"
    }
}
