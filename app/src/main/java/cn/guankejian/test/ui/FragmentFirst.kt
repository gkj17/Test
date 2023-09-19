package cn.guankejian.test.ui

import android.content.ContentUris
import android.content.Context
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.database.getStringOrNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.test.R
import cn.guankejian.test.databinding.FragmentFirstBinding
import cn.guankejian.test.logE
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentFirst @Inject constructor(
) : Fragment(){
  private lateinit var binding: FragmentFirstBinding


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.fragment_first,container,false)
    val list = getStoragePublicUrls(requireContext(), types= *arrayOf(ResourceType.ResourceOther))

    list
    return binding.root
  }


  fun getStoragePublicUrls(
    context: Context,
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null,
    vararg types: ResourceType
  ): List<Uri> {
    return types.map {
      getStoragePublicUrl(context,it,projection, selection, selectionArgs, sortOrder)
    }.flatten()
  }

  fun getStoragePublicUrl(
    context: Context,
    type: ResourceType,
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null
  ): List<Uri> {
    val resultList = ArrayList<Uri>()
    val resolver = context.contentResolver
    val uri = when (type) {
      ResourceType.ResourceAudio -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
      ResourceType.ResourceImage -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
      ResourceType.ResourceVideo -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
      ResourceType.ResourceOther -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Downloads.EXTERNAL_CONTENT_URI
      } else {
        return emptyList()
      }
    }

    val resultCursor = resolver?.query(
      uri,
      projection,
      selection,
      selectionArgs,
      sortOrder
    )
    resultCursor?.use {
      val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//      val id = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
//      val id = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)


      val date_addedColumn  = it.getColumnIndexOrThrow(DATE_ADDED)
      val date_modifiedColumn  = it.getColumnIndexOrThrow(DATE_MODIFIED)
      val date_takenColumn  = it.getColumnIndexOrThrow(DATE_TAKEN)
      val display_nameColumn  = it.getColumnIndexOrThrow(DISPLAY_NAME)
      val durationColumn  = it.getColumnIndexOrThrow(DURATION)
      val heightColumn  = it.getColumnIndexOrThrow(HEIGHT)
      val is_favoriteColumn  = it.getColumnIndexOrThrow(IS_FAVORITE)
      val is_pendingColumn  = it.getColumnIndexOrThrow(IS_PENDING)
      val is_trashedColumn  = it.getColumnIndexOrThrow(IS_TRASHED)
      val mime_typeColumn  = it.getColumnIndexOrThrow(MIME_TYPE)
      val relative_pathColumn  = it.getColumnIndexOrThrow(RELATIVE_PATH)
      val sizeColumn  = it.getColumnIndexOrThrow(SIZE)
      //title没有后缀
      val titleColumn  = it.getColumnIndexOrThrow(TITLE)
      val widthColumn  = it.getColumnIndexOrThrow(WIDTH)
      val descriptionColumn = it.getColumnIndexOrThrow(DESCRIPTION)

      while (it.moveToNext()) {
        val fileId = it.getLong(idColumn)

        val description = it.getStringOrNull(descriptionColumn)

        val display_name = it.getString(display_nameColumn)
        val is_favorite = it.getString(is_favoriteColumn)
        val is_pending = it.getString(is_pendingColumn)
        val is_trashed = it.getString(is_trashedColumn)
        val mime_type = it.getStringOrNull(mime_typeColumn)?:continue
        val relative_path = it.getString(relative_pathColumn)
        val title = it.getString(titleColumn)

        var latitude = 0.0
        var longitude = 0.0
        val duration = it.getLong(durationColumn)
        val date_added = it.getLong(date_addedColumn)*1000
        val date_modified = it.getLong(date_modifiedColumn)*1000
        val date_taken = it.getLong(date_takenColumn)
        val height = it.getLong(heightColumn)
        val size = it.getLong(sizeColumn)
        val width = it.getLong(widthColumn)

        /**
         * ContentUris.withAppendedId() 方法是专门用于将 ID 附加到特定类型的 URI（例如 Content:// URI）上的便捷方法。
         * 如果你正在处理其他类型的 URI，例如 file:// URI 或自定义 URI，那么你可能需要使用 uri.buildUpon().appendPath("$fileId").build() 的方式来构建 URI。
         */
        val pathUri = ContentUris.withAppendedId(uri, fileId)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

          val tmpUri = MediaStore.setRequireOriginal(pathUri)
          resolver.openInputStream(tmpUri)?.use { stream ->
            ExifInterface(stream).latLong?.run {
              latitude = this[0]
              longitude = this[1]
            }
          }
        }else{
          val latitudeColumn = it.getColumnIndexOrThrow(LATITUDE)
          val longitudeColumn = it.getColumnIndexOrThrow(LONGITUDE)
          latitude = it.getDouble(latitudeColumn)
          longitude = it.getDouble(longitudeColumn)
        }


        resultList.add(pathUri)
      }
    }

    return resultList
  }

  /*
  * BOOKMARK
    CATEGORY
    COLOR_RANGE
    COLOR_STANDARD
    COLOR_TRANSFER
    DESCRIPTION
    IS_PRIVATE
    LANGUAGE
  *
  * */

//  open class FileInfo(
//    val name:String,
//    val uri:Uri,
//    val length:Long,
//    val mimeType:String,
//    val dateAdded:Long,
//    val dateModified:Long,
//  )
//
//  class ImageInfo(
//    val width:Int,
//    val height:Int,
//    name: String,
//    uri: Uri,
//    length: Long,
//    mimeType: String,
//    dateAdded: Long,
//    dateModified: Long,
//  ): FileInfo(name, uri, length, mimeType, dateAdded, dateModified)

  sealed class FileInfo(

  ){
    abstract val displayName:String
    abstract val title:String
    abstract val uri:Uri
    abstract val size:Long
    abstract val mimeType:String
    abstract val dateAdded:Long
    abstract val dateModified:Long
    abstract val relativePath:Long

    data class ImageInfo(
      override val displayName: String,
      override val uri: Uri,
      override val size: Long,
      override val mimeType: String,
      override val dateAdded: Long,
      override val dateModified: Long,
      override val title:String,
      val dateTaken:Long,
      val width:Int,
      val height:Int,
      val latitude:Double,
      val longitude:Double,
      val description:String,
      val is_favorite:String,
      val is_trashed:String,
      override val relativePath: Long,
    ) : FileInfo()
    data class VideoInfo(
      override val displayName: String,
      override val uri: Uri,
      override val size: Long,
      override val mimeType: String,
      override val dateAdded: Long,
      override val dateModified: Long,
      override val relativePath: Long,
      override val title:String,
      val description:String,
      val is_favorite:String,
      val is_trashed:String,
      val latitude:Double,
      val longitude:Double,
      val duration:String,
      val height:Int,
      val width:Int,
    ) : FileInfo()
    data class AudioInfo(
      override val displayName: String,
      override val uri: Uri,
      override val size: Long,
      override val relativePath: Long,
      override val mimeType: String,
      override val dateAdded: Long,
      override val dateModified: Long,
      override val title:String,
      val duration:String,
    ) : FileInfo()
    data class OtherInfo(
      override val displayName: String,
      override val uri: Uri,
      override val size: Long,
      override val mimeType: String,
      override val title:String,
      override val dateAdded: Long,
      override val relativePath: Long,
      override val dateModified: Long
    ) : FileInfo()
  }





  sealed class ResourceType {
    object ResourceImage : ResourceType()
    object ResourceVideo : ResourceType()
    object ResourceAudio : ResourceType()
    object ResourceOther : ResourceType()
  }

}