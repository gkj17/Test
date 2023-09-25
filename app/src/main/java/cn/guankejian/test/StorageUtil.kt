package cn.guankejian.test

import android.content.Context

/**
 *  Created by guank on 2023/9/24 15:12
 *  Email       :   guan_kejian@163.com
 *  Version     :   1.0
 *  Description :
 **/
class StorageUtil {

  companion object {
    @JvmStatic
    fun readAllFile(showHidden: Boolean = false): List<FileItem> {
      return readAllFileInner(showHidden = showHidden)
    }

    @JvmStatic
    fun readPublicStorage(context: Context,vararg type: ResourceType): List<PublicStorageFileInfo> {
      return readPublicStorageInner(context,*type)
    }

    @JvmStatic
    fun readPublicStorageMedia(context: Context): List<PublicStorageFileInfo> {
      return readPublicStorage(
        context,
        ResourceType.ResourceImage,
        ResourceType.ResourceVideo,
        ResourceType.ResourceAudio
      )
    }

    @JvmStatic
    fun readAllFileByGroup(showHidden: Boolean = false): Map<String, List<FileItem>> {
      return readAllFile(showHidden = showHidden).groupBy {
        it.parentPath
      }
    }

    @JvmStatic
    fun readPublicStorageByGroup(context: Context): Map<String, List<PublicStorageFileInfo>> {
      return readPublicStorage(context).groupBy { info ->
        info.parentPath
          .replace("/pictures", "/Pictures")
          .replace("/dcim", "/DCIM")
          .replace("/camera", "/Camera")
          .replace("/screenshots", "/Screenshots")
      }
    }

    @JvmStatic
    fun readPublicStorageMediaByGroup(context: Context): Map<String, List<PublicStorageFileInfo>> {
      return readPublicStorageMedia(context).groupBy { info ->
        info.parentPath
          .replace("/pictures", "/Pictures")
          .replace("/dcim", "/DCIM")
          .replace("/camera", "/Camera")
          .replace("/screenshots", "/Screenshots")
      }
    }
  }
}