package cn.guankejian.test.ui

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.database.getStringOrNull
import androidx.databinding.DataBindingUtil
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.test.R
import cn.guankejian.test.databinding.FragmentFirstBinding


@ExperimentalPagingApi
class FragmentFirst constructor(
) : Fragment() {
  private lateinit var binding: FragmentFirstBinding


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    binding = DataBindingUtil.inflate(
      LayoutInflater.from(requireContext()),
      R.layout.fragment_first,
      container,
      false
    )
    val imageList: MutableList<FilesInfo.ImageInfo> = getImageInfoList(requireContext()).toMutableList()
    val videoList: MutableList<FilesInfo.VideoInfo> = getVideoInfoList(requireContext()).toMutableList()
    val audioList: MutableList<FilesInfo.AudioInfo> = getAudioInfoList(requireContext()).toMutableList()
    val otherList: MutableList<FilesInfo.OtherInfo> = ArrayList()
    getDownloadInfoList(requireContext()).map {
      when(it.type){
        is ResourceType.ResourceImage-> imageList.add(it.item as FilesInfo.ImageInfo)
        is ResourceType.ResourceVideo-> videoList.add(it.item as FilesInfo.VideoInfo)
        is ResourceType.ResourceAudio-> audioList.add(it.item as FilesInfo.AudioInfo)
        is ResourceType.ResourceOther-> otherList.add(it.item as FilesInfo.OtherInfo)
      }
    }

//    downloadList
    "123"
    return binding.root
  }


  fun getVideoInfoList(context: Context ,projection: Array<String>? = null,selection: String? = null,selectionArgs: Array<String>? = null,sortOrder: String? = null): List<FilesInfo.VideoInfo> {
    val resolver = context.contentResolver
    val resultList = ArrayList<FilesInfo.VideoInfo>()
    val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    val resultCursor = resolver.query(uri,projection,selection,selectionArgs,sortOrder)
    resultCursor?.use {
      val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)

      val displayNameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
      val titleColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
      val mimeTypeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
      val descriptionColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION)
      val dateAddedColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
      val dateModifiedColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
      val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
      val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
      val heightColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
      val widthColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)



      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val relativePathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
        while (it.moveToNext()) {
          getVideoInfo(it,idColumn, displayNameColumn, titleColumn, mimeTypeColumn, descriptionColumn, dateAddedColumn, dateModifiedColumn, relativePathColumn, sizeColumn, durationColumn, heightColumn, widthColumn, uri, resolver, resultList)
        }
      }else{
        val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        while (it.moveToNext()) {
          getVideoInfoLessThanQ(it,idColumn, displayNameColumn, titleColumn, mimeTypeColumn, descriptionColumn, dateAddedColumn, dateModifiedColumn, dataColumn, sizeColumn, durationColumn, heightColumn, widthColumn, uri, resolver, resultList)
        }
      }


    }

    return resultList
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  private fun getVideoInfo(cursor: Cursor, idColumn: Int, displayNameColumn: Int, titleColumn: Int, mimeTypeColumn: Int, descriptionColumn: Int, dateAddedColumn: Int, dateModifiedColumn: Int, relativePathColumn: Int, sizeColumn: Int, durationColumn: Int, heightColumn: Int, widthColumn: Int, uri: Uri, resolver: ContentResolver, resultList: ArrayList<FilesInfo.VideoInfo>, needLocation:Boolean=false) {
    val id = cursor.getLong(idColumn)
    val displayName = cursor.getString(displayNameColumn)
    val title = cursor.getString(titleColumn)
    val mimeType = cursor.getStringOrNull(mimeTypeColumn) ?: return
    val description = cursor.getStringOrNull(descriptionColumn) ?: ""
    val dateAdded = cursor.getLong(dateAddedColumn)*1000
    val dateModified = cursor.getLong(dateModifiedColumn)*1000
    val relativePath = cursor.getString(relativePathColumn)
    val size = cursor.getLong(sizeColumn)
    val duration = cursor.getLong(durationColumn)
    val height = cursor.getInt(heightColumn)
    val width = cursor.getInt(widthColumn)
    var latitude = 0.0
    var longitude = 0.0

    /**
     * 也可以用 Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,cursor.getString(idColumnIndex))
     */
    val pathUri = ContentUris.withAppendedId(uri, id)


    if(needLocation){
      val tmpUri = MediaStore.setRequireOriginal(pathUri)
      resolver.openInputStream(tmpUri)?.use { stream ->
        ExifInterface(stream).latLong?.run {
          latitude = this[0]
          longitude = this[1]
        }
      }
    }



    val item = FilesInfo.VideoInfo(pathUri,displayName,title,mimeType,description,relativePath,dateAdded,dateModified,size,duration,height,width,latitude,longitude)

    resultList.add(item)
  }

  private fun getVideoInfoLessThanQ(cursor: Cursor, idColumn: Int, displayNameColumn: Int, titleColumn: Int, mimeTypeColumn: Int, descriptionColumn: Int, dateAddedColumn: Int, dateModifiedColumn: Int, dataColumn: Int, sizeColumn: Int, durationColumn: Int, heightColumn: Int, widthColumn: Int, uri: Uri, resolver: ContentResolver, resultList: ArrayList<FilesInfo.VideoInfo>) {
    val id = cursor.getLong(idColumn)
    val displayName = cursor.getString(displayNameColumn)
    val dateAdded = cursor.getLong(dateAddedColumn)*1000
    val mimeType = cursor.getStringOrNull(mimeTypeColumn) ?: return
    val title = cursor.getString(titleColumn)
    val realDisplayName = title+displayName.substring(displayName.indexOf("."))
    val size = cursor.getLong(sizeColumn)
    val dateModified = cursor.getLong(dateModifiedColumn)*1000
    val relativePath = cursor.getString(dataColumn).replace(Environment.getExternalStorageDirectory().absolutePath+"/","").replace(realDisplayName,"")
    val duration = cursor.getLong(durationColumn)

    val description = cursor.getStringOrNull(descriptionColumn) ?: ""
    val width = cursor.getInt(widthColumn)
    val height = cursor.getInt(heightColumn)
    var latitude = 0.0
    var longitude = 0.0


    /**
     * 也可以用 Uri.withAppendedPath(MediaStore.Images.Video.EXTERNAL_CONTENT_URI,cursor.getString(idColumnIndex))
     */
    val pathUri = ContentUris.withAppendedId(uri, id)
    resolver.openInputStream(pathUri)?.use { stream ->
      ExifInterface(stream).latLong?.run {
        latitude = this[0]
        longitude = this[1]
      }
    }
    val item = FilesInfo.VideoInfo(pathUri,displayName,title,mimeType,description,relativePath,dateAdded,dateModified,size,duration,height,width,latitude,longitude)

    resultList.add(item)
  }

  fun getImageInfoList(context: Context,projection: Array<String>? = null,selection: String? = null,selectionArgs: Array<String>? = null,sortOrder: String? = null): List<FilesInfo.ImageInfo> {
    val resolver = context.contentResolver
    val resultList = ArrayList<FilesInfo.ImageInfo>()
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val resultCursor = resolver.query(uri,projection,selection,selectionArgs,sortOrder)
    resultCursor?.use {
      val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

      val displayNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
      val dateAddedColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
      val mimeTypeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
      val titleColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
      val descriptionColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION)
      val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
      val dateModifiedColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
      val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
      val widthColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
      val heightColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val relativePathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
        while (it.moveToNext()) {
          getImageInfo(it,idColumn,displayNameColumn,dateAddedColumn,mimeTypeColumn,titleColumn,descriptionColumn,sizeColumn,dateModifiedColumn,dateTakenColumn,relativePathColumn,widthColumn,heightColumn,uri,resolver,resultList)
        }
      }else{
        val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        while (it.moveToNext()) {
          getImageInfoLessThanQ(it,idColumn,displayNameColumn,dateAddedColumn,mimeTypeColumn,titleColumn,descriptionColumn,sizeColumn,dateModifiedColumn,dateTakenColumn,dataColumn,widthColumn,heightColumn,uri,resolver,resultList)
        }
      }

    }

    return resultList
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  private fun getImageInfo(cursor: Cursor, idColumn: Int, displayNameColumn: Int, dateAddedColumn: Int, mimeTypeColumn: Int, titleColumn: Int, descriptionColumn: Int, sizeColumn: Int, dateModifiedColumn: Int, dateTakenColumn: Int, relativePathColumn: Int, widthColumn: Int, heightColumn: Int, uri: Uri, resolver: ContentResolver, resultList: MutableList<FilesInfo.ImageInfo>, needLocation:Boolean=false) {
    val id = cursor.getLong(idColumn)
    val displayName = cursor.getString(displayNameColumn)
    val dateAdded = cursor.getLong(dateAddedColumn)*1000
    val mimeType = cursor.getStringOrNull(mimeTypeColumn) ?: return
    val title = cursor.getString(titleColumn)
    val description = cursor.getStringOrNull(descriptionColumn) ?: ""
    val size = cursor.getLong(sizeColumn)
    val dateModified = cursor.getLong(dateModifiedColumn)*1000
    val dateTaken = cursor.getLong(dateTakenColumn)
    val relativePath = cursor.getString(relativePathColumn)
    val width = cursor.getInt(widthColumn)
    val height = cursor.getInt(heightColumn)
    var latitude = 0.0
    var longitude = 0.0

    /**
     * 也可以用 Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cursor.getString(idColumnIndex))
     */
    val pathUri = ContentUris.withAppendedId(uri, id)
    if(needLocation) {
      val tmpUri = MediaStore.setRequireOriginal(pathUri)
      resolver.openInputStream(tmpUri)?.use { stream ->
        ExifInterface(stream).latLong?.run {
          latitude = this[0]
          longitude = this[1]
        }
      }
    }
    resultList.add(FilesInfo.ImageInfo(pathUri,displayName,dateAdded,mimeType,title,description,relativePath,size,dateModified,dateTaken,width,height,latitude,longitude))
  }

  private fun getImageInfoLessThanQ(cursor: Cursor, idColumn: Int, displayNameColumn: Int, dateAddedColumn: Int, mimeTypeColumn: Int, titleColumn: Int, descriptionColumn: Int, sizeColumn: Int, dateModifiedColumn: Int, dateTakenColumn: Int, dataColumn: Int, widthColumn: Int, heightColumn: Int, uri: Uri, resolver: ContentResolver, resultList: MutableList<FilesInfo.ImageInfo>) {
    val id = cursor.getLong(idColumn)
    val displayName = cursor.getString(displayNameColumn)
    val dateAdded = cursor.getLong(dateAddedColumn)*1000
    val mimeType = cursor.getStringOrNull(mimeTypeColumn) ?: return
    val title = cursor.getString(titleColumn)
    val realDisplayName = title+displayName.substring(displayName.indexOf("."))
    val description = cursor.getStringOrNull(descriptionColumn) ?: ""
    val size = cursor.getLong(sizeColumn)
    val dateModified = cursor.getLong(dateModifiedColumn)*1000
    val dateTaken = cursor.getLong(dateTakenColumn)
    val relativePath = cursor.getString(dataColumn).replace(Environment.getExternalStorageDirectory().absolutePath+"/","").replace(realDisplayName,"")
    val width = cursor.getInt(widthColumn)
    val height = cursor.getInt(heightColumn)
    var latitude = 0.0
    var longitude = 0.0


    /**
     * 也可以用 Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cursor.getString(idColumnIndex))
     */
    val pathUri = ContentUris.withAppendedId(uri, id)
    resolver.openInputStream(pathUri)?.use { stream ->
      ExifInterface(stream).latLong?.run {
        latitude = this[0]
        longitude = this[1]
      }
    }
    resultList.add(FilesInfo.ImageInfo(pathUri,displayName,dateAdded,mimeType,title,description,relativePath,size,dateModified,dateTaken,width,height,latitude,longitude))

  }

  fun getAudioInfoList(context: Context,projection: Array<String>? = null,selection: String? = null,selectionArgs: Array<String>? = null,sortOrder: String? = null): List<FilesInfo.AudioInfo> {
    val resolver = context.contentResolver
    val resultList = ArrayList<FilesInfo.AudioInfo>()
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    val resultCursor = resolver.query(uri,projection,selection,selectionArgs,sortOrder)
    resultCursor?.use {
      val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

      val displayNameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
      val mimeTypeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
      val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
      val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
      val dateAddedColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
      val dateModifiedColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)
      val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)



      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val relativePathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
        while (it.moveToNext()) {
          getAudioInfo(it,idColumn, displayNameColumn, mimeTypeColumn, titleColumn, relativePathColumn, sizeColumn, dateAddedColumn, dateModifiedColumn, durationColumn, uri, resultList)
        }
      }else{
        val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        while (it.moveToNext()) {
          getAudioInfoLessThanQ(it,idColumn, displayNameColumn, mimeTypeColumn, titleColumn, dataColumn, sizeColumn, dateAddedColumn, dateModifiedColumn, durationColumn, uri, resultList)
        }
      }
    }

    return resultList
  }

  private fun getAudioInfo(cursor: Cursor, idColumn: Int, displayNameColumn: Int, mimeTypeColumn: Int, titleColumn: Int, relativePathColumn: Int, sizeColumn: Int, dateAddedColumn: Int, dateModifiedColumn: Int, durationColumn: Int, uri: Uri, resultList: ArrayList<FilesInfo.AudioInfo>) {
    val id = cursor.getLong(idColumn)

    val displayName = cursor.getString(displayNameColumn)
    val mimeType = cursor.getStringOrNull(mimeTypeColumn) ?: return
    val title = cursor.getString(titleColumn)
    val relativePath = cursor.getString(relativePathColumn)
    val size = cursor.getLong(sizeColumn)
    val dateAdded = cursor.getLong(dateAddedColumn)*1000
    val dateModified = cursor.getLong(dateModifiedColumn)*1000
    val duration = cursor.getLong(durationColumn)


    /**
     * 也可以用 Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getString(idColumnIndex))
     */
    val pathUri = ContentUris.withAppendedId(uri, id)

    val item = FilesInfo.AudioInfo(pathUri,displayName,mimeType,title,relativePath,size,dateAdded,dateModified,duration)

    resultList.add(item)
  }

  private fun getAudioInfoLessThanQ(cursor: Cursor, idColumn: Int, displayNameColumn: Int, mimeTypeColumn: Int, titleColumn: Int, dataColumn: Int, sizeColumn: Int, dateAddedColumn: Int, dateModifiedColumn: Int, durationColumn: Int, uri: Uri, resultList: ArrayList<FilesInfo.AudioInfo>) {
    val id = cursor.getLong(idColumn)
    val displayName = cursor.getString(displayNameColumn)
    val dateAdded = cursor.getLong(dateAddedColumn)*1000
    val mimeType = cursor.getStringOrNull(mimeTypeColumn) ?: return
    val title = cursor.getString(titleColumn)
    val realDisplayName = title+displayName.substring(displayName.indexOf("."))
    val size = cursor.getLong(sizeColumn)
    val dateModified = cursor.getLong(dateModifiedColumn)*1000
    val relativePath = cursor.getString(dataColumn).replace(Environment.getExternalStorageDirectory().absolutePath+"/","").replace(realDisplayName,"")
    val duration = cursor.getLong(durationColumn)



    /**
     * 也可以用 Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getString(idColumnIndex))
     */
    val pathUri = ContentUris.withAppendedId(uri, id)
    val item = FilesInfo.AudioInfo(pathUri,displayName,mimeType,title,relativePath,size,dateAdded,dateModified,duration)

    resultList.add(item)
  }

  private fun getOtherInfo(cursor: Cursor, idColumn: Int, displayNameColumn: Int, mimeTypeColumn: Int, titleColumn: Int, relativePathColumn: Int, sizeColumn: Int, dateAddedColumn: Int, dateModifiedColumn: Int, uri: Uri, resultList: ArrayList<FilesInfo.OtherInfo>) {
    val id = cursor.getLong(idColumn)

    val displayName = cursor.getString(displayNameColumn)
    val mimeType = cursor.getStringOrNull(mimeTypeColumn) ?: return
    val title = cursor.getString(titleColumn)
    val relativePath = cursor.getString(relativePathColumn)
    val size = cursor.getLong(sizeColumn)
    val dateAdded = cursor.getLong(dateAddedColumn)*1000
    val dateModified = cursor.getLong(dateModifiedColumn)*1000



    val pathUri = ContentUris.withAppendedId(uri, id)

    val item = FilesInfo.OtherInfo(pathUri,displayName,mimeType,title,relativePath,size,dateAdded,dateModified,)

    resultList.add(item)
  }

  fun getDownloadInfoList(context: Context ,projection: Array<String>? = null,selection: String? = null,selectionArgs: Array<String>? = null,sortOrder: String? = null): List<FileInfo> {
    val resolver = context.contentResolver

    val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      MediaStore.Downloads.EXTERNAL_CONTENT_URI
    } else {
      return emptyList()
    }

    val resultList = ArrayList<FileInfo>()

    val imageResultList = ArrayList<FilesInfo.ImageInfo>()
    val videoResultList = ArrayList<FilesInfo.VideoInfo>()
    val audioResultList = ArrayList<FilesInfo.AudioInfo>()
    val otherResultList = ArrayList<FilesInfo.OtherInfo>()

    val resultCursor = resolver.query(uri,projection,selection,selectionArgs,sortOrder)

    resultCursor?.use {
      val imageIdColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)}
      val imageDisplayNameColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)}
      val imageDateAddedColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)}
      val imageMimeTypeColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)}
      val imageTitleColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)}
      val imageDescriptionColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION)}
      val imageSizeColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)}
      val imageDateModifiedColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)}
      val imageDateTakenColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)}
      val imageRelativePathColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)}
      val imageWidthColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)}
      val imageHeightColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)}


      val audioIdColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)}
      val audioDisplayNameColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)}
      val audioMimeTypeColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)}
      val audioTitleColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)}
      val audioRelativePathColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Audio.Media.RELATIVE_PATH)}
      val audioSizeColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)}
      val audioDateAddedColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)}
      val audioDateModifiedColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)}
      val audioDurationColumn by lazy{ it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)}

      val videoIdColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)}
      val videoDisplayNameColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)}
      val videoTitleColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)}
      val videoMimeTypeColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)}
      val videoDescriptionColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION)}
      val videoDateAddedColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)}
      val videoDateModifiedColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)}
      val videoRelativePathColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH)}
      val videoSizeColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)}
      val videoDurationColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)}
      val videoHeightColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)}
      val videoWidthColumn by lazy{it.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)}

      val otherIdColumn by lazy { it.getColumnIndexOrThrow(MediaStore.Downloads._ID)}
      val otherDisplayNameColumn by lazy { it.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)}
      val otherMimeTypeColumn by lazy { it.getColumnIndexOrThrow(MediaStore.Downloads.MIME_TYPE)}
      val otherTitleColumn by lazy { it.getColumnIndexOrThrow(MediaStore.Downloads.TITLE)}
      val otherRelativePathColumn by lazy { it.getColumnIndexOrThrow(MediaStore.Downloads.RELATIVE_PATH)}
      val otherSizeColumn by lazy { it.getColumnIndexOrThrow(MediaStore.Downloads.SIZE)}
      val otherDateAddedColumn by lazy { it.getColumnIndexOrThrow(MediaStore.Downloads.DATE_ADDED)}
      val otherDateModifiedColumn by lazy { it.getColumnIndexOrThrow(MediaStore.Downloads.DATE_MODIFIED)}


      while (it.moveToNext()) {
        val mimeType = it.getStringOrNull(otherMimeTypeColumn) ?: continue

        if (mimeType.contains("image")) {
          getImageInfo(it,imageIdColumn,imageDisplayNameColumn,imageDateAddedColumn,imageMimeTypeColumn,imageTitleColumn,imageDescriptionColumn,imageSizeColumn,imageDateModifiedColumn,imageDateTakenColumn,imageRelativePathColumn,imageWidthColumn,imageHeightColumn,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,resolver,imageResultList)
        } else if (mimeType.contains("video")) {
          getVideoInfo(it,videoIdColumn,videoDisplayNameColumn,videoTitleColumn,videoMimeTypeColumn,videoDescriptionColumn,videoDateAddedColumn,videoDateModifiedColumn,videoRelativePathColumn,videoSizeColumn,videoDurationColumn,videoHeightColumn,videoWidthColumn,MediaStore.Video.Media.EXTERNAL_CONTENT_URI,resolver,videoResultList)
        } else if (mimeType.contains("audio")) {
          getAudioInfo(it,audioIdColumn,audioDisplayNameColumn,audioMimeTypeColumn,audioTitleColumn,audioRelativePathColumn,audioSizeColumn,audioDateAddedColumn,audioDateModifiedColumn,audioDurationColumn,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,audioResultList)
        } else {
          getOtherInfo(it,otherIdColumn,otherDisplayNameColumn,otherMimeTypeColumn,otherTitleColumn,otherRelativePathColumn,otherSizeColumn,otherDateAddedColumn,otherDateModifiedColumn,MediaStore.Downloads.EXTERNAL_CONTENT_URI,otherResultList)
        }


      }
    }

    listOf(imageResultList,videoResultList,audioResultList,otherResultList).flatten().map {
      when(it){
        is FilesInfo.ImageInfo-> resultList.add(FileInfo(ResourceType.ResourceImage,it))
        is FilesInfo.VideoInfo-> resultList.add(FileInfo(ResourceType.ResourceVideo,it))
        is FilesInfo.AudioInfo-> resultList.add(FileInfo(ResourceType.ResourceAudio,it))
        is FilesInfo.OtherInfo-> resultList.add(FileInfo(ResourceType.ResourceOther,it))
      }
    }

    return resultList
  }

  sealed class FilesInfo {
    data class ImageInfo(
      val uri: Uri,
      val displayName: String,
      val dateAdded: Long,
      val mimeType: String,
      val title: String,
      val description: String,
      val relativePath: String,
      val size: Long,
      val dateModified: Long,
      val dateTaken: Long,
      val width: Int,
      val height: Int,
      val latitude: Double,
      val longitude: Double,
    ) : FilesInfo()

    data class VideoInfo(
      val uri: Uri,
      val displayName: String,
      val title: String,
      val mimeType: String,
      val description: String,
      val relativePath: String,
      val dateAdded: Long,
      val dateModified: Long,
      val size: Long,
      val duration: Long,
      val height: Int,
      val width: Int,
      val latitude: Double,
      val longitude: Double,
    ) : FilesInfo()

    data class AudioInfo(
      val uri: Uri,
      val displayName: String,
      val mimeType: String,
      val title: String,
      val relativePath: String,
      val size: Long,
      val dateAdded: Long,
      val dateModified: Long,
      val duration: Long,
    ) : FilesInfo()

    data class OtherInfo(
      val uri: Uri,
      val displayName: String,
      val mimeType: String,
      val title: String,
      val relativePath: String,
      val size: Long,
      val dateAdded: Long,
      val dateModified: Long,
    ) : FilesInfo()
  }

  class FileInfo(
    val type: ResourceType,
    val item: Any
  )


  sealed class ResourceType {
    object ResourceImage : ResourceType()
    object ResourceVideo : ResourceType()
    object ResourceAudio : ResourceType()
    object ResourceOther : ResourceType()
  }

}