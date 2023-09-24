package cn.guankejian.lib_common.util.storage

import android.os.Environment
import java.io.File
import java.util.LinkedList

/**
 *  Created by guank on 2023/9/24 15:08
 *  Email       :   guan_kejian@163.com
 *  Version     :   1.0
 *  Description :
 **/
sealed class FileItem {
  abstract val name: String
  val parentPath by lazy {
    path.substring(0, path.lastIndexOf("/"))
  }
  abstract val path: String
  abstract val lastModified: Long

  data class File(
    override val name: String,
    override val path: String,
    override val lastModified: Long,
    val size: Long
  ) : FileItem()

  data class Folder(
    override val name: String,
    override val path: String,
    override val lastModified: Long
  ) : FileItem()
}



fun readAllFileInner(
  directory: File = Environment.getExternalStorageDirectory(),
  showHidden: Boolean = false
): List<FileItem> {
  val listFile = directory.listFiles() ?: return emptyList()
  val ret = LinkedList<FileItem>()
  for (item in listFile) {
    if (item.isDirectory) {
      if (item.isHidden && !showHidden) continue
      val name = item.name
      val lastModified = item.lastModified()
      val path = item.absolutePath
      ret.add(FileItem.Folder(name, path, lastModified))
      ret.addAll(readAllFileInner(item, showHidden))
    } else {
      if (item.isHidden && !showHidden) continue
      val name = item.name
      val lastModified = item.lastModified()
      val path = item.absolutePath
      val size = item.length()
      ret.add(FileItem.File(name, path, lastModified, size))
    }
  }
  return ret
}
