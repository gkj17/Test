package cn.guankejian.test.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.guankejian.test.DateUtil
import cn.guankejian.test.db.MeiNvDatabase



@Entity(tableName = MeiNvDatabase.TABLE_NAME_MZ_TAG, primaryKeys = ["id"])
data class MZTag(
    var id: Long?,
    var price: Double = 0.00,
    var remark: String = "",
    var digest: String = "",
    var type: String = "",
    var in_out: String = "",
    var path: String = "",
    var year: Int = DateUtil.currentYear(),
    var month: Int = DateUtil.currentMonth(),
    var day: Int = DateUtil.currentDay()
) {
    override fun toString(): String {
        return "id:${id},price:${price},remark:${remark},type:${type},in_out:${in_out},year:${year},month:${month},day:${day},path:${path},digest:${digest}"
    }
}