package cn.guankejian.test

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import cn.guankejian.test.bean.ConstantKey
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.db.ConstantDao
import cn.guankejian.test.db.MeiNvDatabase
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
class TagMediator(
    val db: MeiNvDatabase,
    val KEY: String,
    val minuteDuration: Long = 1440
) : RemoteMediator<Int, MZTag>() {
    companion object {
        val LISTs = listOf<MZTag>(
            MZTag(1, 4314.13, "First Statistics", "", "Other", "in", "WeChat Pay", 2022, 4, 23),
            MZTag(2, 54.48, "First Statistics", "", "Other", "in", "支付宝", 2022, 4, 23),
            MZTag(3, 8.4, "First Statistics", "", "Other", "in", "建行211", 2022, 4, 23),
            MZTag(4, 0.0, "First Statistics", "", "Other", "in", "招行389", 2022, 4, 23),
            MZTag(5, 465.27, "First Statistics", "", "Other", "in", "中行439", 2022, 4, 23),
            MZTag(6, 497.49, "First Statistics", "", "Other", "in", "工行343", 2022, 4, 23),
            MZTag(7, 0.0, "First Statistics", "", "Other", "in", "中行496", 2022, 4, 23),
            MZTag(8, 1455.71, "First Statistics", "", "Other", "in", "现金", 2022, 4, 23),
            MZTag(9, 10000.0, "", "借入", "Other", "in", "工行343", 2022, 4, 23),
            MZTag(10, 30.6, "", "Dinner 汤饭", "Food", "out", "工行343", 2022, 4, 23),
            MZTag(11, 10.0, "", "Breakfast 田料", "Food", "out", "工行343", 2022, 4, 24),
            MZTag(12, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 24),
            MZTag(13, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 24),
            MZTag(14, 9.8, "", "李子", "Food", "out", "工行343", 2022, 4, 24),
            MZTag(15, 30.0, "", "Dinner 爱鱼者", "Food", "out", "工行343", 2022, 4, 24),
            MZTag(16, 500.0, "王老师发劳务", "研究生补贴", "Salary", "in", "工行343", 2022, 4, 24),
            MZTag(17, 5.0, "", "Breakfast 酱香饼", "Food", "out", "工行343", 2022, 4, 25),
            MZTag(18, 7.78, "", "午餐 烧鸭", "Food", "out", "工行343", 2022, 4, 25),
            MZTag(19, 4.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 25),
            MZTag(20, 3.0, "", "菠萝", "Food", "out", "WeChat Pay", 2022, 4, 25),
            MZTag(21, 30.0, "", "Dinner 忠记", "Food", "out", "工行343", 2022, 4, 25),
            MZTag(22, 4.6, "", "香蕉", "Food", "out", "工行343", 2022, 4, 25),
            MZTag(23, 320.0, "", "帮啊珠刷单", "Other", "out", "工行343", 2022, 4, 25),
            MZTag(24, 320.0, "", "刷单回款", "Other", "in", "WeChat Pay", 2022, 4, 25),
            MZTag(25, 5.0, "", "Breakfast 酱香饼", "Food", "out", "工行343", 2022, 4, 26),
            MZTag(26, 4.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 26),
            MZTag(27, 3.0, "", "地铁", "Walk", "out", "工行343", 2022, 4, 26),
            MZTag(28, 2.0, "", "公交", "Walk", "out", "工行343", 2022, 4, 26),
            MZTag(29, 16.0, "", "午餐", "Food", "out", "工行343", 2022, 4, 26),
            MZTag(30, 12.87, "", "午餐 麻辣牛肉", "Food", "out", "工行343", 2022, 4, 26),
            MZTag(31, 2.0, "", "公交", "Walk", "out", "工行343", 2022, 4, 26),
            MZTag(32, 3.5, "", "可乐", "Food", "out", "工行343", 2022, 4, 26),
            MZTag(33, 3.0, "", "地铁", "Walk", "out", "工行343", 2022, 4, 26),
            MZTag(34, 4.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 26),
            MZTag(35, 24.0, "", "Dinner 螺蛳粉", "Food", "out", "工行343", 2022, 4, 26),
            MZTag(36, 800.0, "", "研究生补贴", "Salary", "in", "工行343", 2022, 4, 26),
            MZTag(37, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 27),
            MZTag(38, 1.0, "", "Breakfast 华夫饼", "Food", "out", "工行343", 2022, 4, 27),
            MZTag(39, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 27),
            MZTag(40, 11.0, "", "午餐 手抓饼", "Food", "out", "工行343", 2022, 4, 27),
            MZTag(41, 16.0, "", "Dinner 云吞", "Food", "out", "工行343", 2022, 4, 27),
            MZTag(42, 8.0, "", "苹果醋", "Food", "out", "工行343", 2022, 4, 27),
            MZTag(43, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 28),
            MZTag(44, 4.0, "", "Breakfast  鸡蛋饼 豆浆", "Food", "out", "工行343", 2022, 4, 28),
            MZTag(45, 1.0, "", "Breakfast 面包", "Food", "out", "工行343", 2022, 4, 28),
            MZTag(46, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 28),
            MZTag(47, 29.0, "", "生蚝 面筋", "Food", "out", "工行343", 2022, 4, 28),
            MZTag(48, 23.0, "", "书亦烧仙草", "Food", "out", "工行343", 2022, 4, 28),
            MZTag(49, 1500.0, "志兵", "借出", "Other", "out", "工行343", 2022, 4, 28),
            MZTag(50, 4.5, "", "Breakfast 田料 面包", "Food", "out", "工行343", 2022, 4, 29),
            MZTag(51, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 29),
            MZTag(52, 15.0, "", "午餐", "Food", "out", "工行343", 2022, 4, 29),
            MZTag(53, 16.3, "", "午餐", "Food", "out", "工行343", 2022, 4, 29),
            MZTag(54, 28.0, "", "Dinner 忠记", "Food", "out", "工行343", 2022, 4, 29),
            MZTag(55, 26.8, "", "水果", "Food", "out", "工行343", 2022, 4, 29),
            MZTag(56, 357.0, "观己公司", "柜子出款", "Other", "out", "工行343", 2022, 4, 29),
            MZTag(57, 357.0, "观己公司", "柜子回款", "Other", "in", "WeChat Pay", 2022, 4, 29),
            MZTag(58, 7.5, "", "饮料", "Food", "out", "工行343", 2022, 4, 29),
            MZTag(59, 531.0, "210片", "隐形眼镜", "Make up", "out", "工行343", 2022, 4, 29),
            MZTag(60, 8.5, "", "Breakfast 田料", "Food", "out", "工行343", 2022, 4, 30),
            MZTag(61, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 30),
            MZTag(62, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 4, 30),
            MZTag(63, 2.8, "", "午餐 生菜 香菜", "Food", "out", "工行343", 2022, 4, 30),
            MZTag(64, 28.48, "", "Dinner 三叔粥", "Food", "out", "工行343", 2022, 4, 30),
            MZTag(65, 30.84, "", "午餐 汤饭", "Food", "out", "工行343", 2022, 5, 1),
            MZTag(66, 1297.05, "房租1200 水费20.51 电费76.54", "房租水电", "Live", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(67, 4.0, "", "电瓶车", "Walk", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(68, 100.0, "", "2件T恤", "Clothes", "out", "工行343", 2022, 5, 1),
            MZTag(69, 23.0, "", "鸡蛋仔 奶茶", "Food", "out", "工行343", 2022, 5, 1),
            MZTag(70, 520.0, "", "2条裤子 1件外套", "Clothes", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(71, 306.0, "", "紫衬衫 黑西装裤", "Clothes", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(72, 160.0, "", "可爱的连Clothes裙", "Clothes", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(73, 39.0, "", "灰色运动裤", "Clothes", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(74, 80.0, "", "尖头鞋", "Clothes", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(75, 208.0, "", "条纹t和黑色牛仔裙", "Clothes", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(76, 10.0, "", "2根烤肠", "Food", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(77, 3.0, "", "矿泉水", "Food", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(78, 4.0, "", "地铁", "Walk", "out", "工行343", 2022, 5, 1),
            MZTag(79, 4.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 5, 1),
            MZTag(80, 4.0, "", "地铁", "Walk", "out", "WeChat Pay", 2022, 5, 1),
            MZTag(81, 17.83, "", "抢Red Packet", "Red Packet", "in", "WeChat Pay", 2022, 5, 1),
            MZTag(82, 23.26, "", "抢Red Packet", "Red Packet", "in", "WeChat Pay", 2022, 5, 1),
            MZTag(83, 18.92, "", "抢Red Packet", "Red Packet", "in", "WeChat Pay", 2022, 5, 1),
            MZTag(84, 33.48, "", "抢Red Packet", "Red Packet", "in", "WeChat Pay", 2022, 5, 1),
            MZTag(85, 33.75, "", "Dinner  兰州拉面", "Food", "out", "工行343", 2022, 5, 1),
            MZTag(86, 12.0, "", "午餐 汤饭", "Food", "out", "WeChat Pay", 2022, 5, 2),
            MZTag(87, 2.0, "", "电瓶车", "Walk", "out", "工行343", 2022, 5, 2),
            MZTag(88, 6.0, "", "地铁 柯木塱", "Walk", "out", "工行343", 2022, 5, 2),
            MZTag(89, 2.0, "", "公交 龙洞", "Walk", "out", "工行343", 2022, 5, 2),
            MZTag(90, 20.0, "", "牛仔裤", "Clothes", "out", "工行343", 2022, 5, 2),
            MZTag(91, 39.0, "", "T恤", "Clothes", "out", "工行343", 2022, 5, 2),
            MZTag(92, 15.0, "", "短裤", "Clothes", "out", "工行343", 2022, 5, 2),
            MZTag(93, 49.0, "", "白色短裤", "Clothes", "out", "工行343", 2022, 5, 2),
            MZTag(94, 56.0, "请妹妹吃饭", "吃饭", "Food", "out", "工行343", 2022, 5, 2),
            MZTag(95, 2.0, "", "电瓶车", "Walk", "out", "WeChat Pay", 2022, 5, 2),
            MZTag(96, 5.0, "", "地铁", "Walk", "out", "工行343", 2022, 5, 2),
            MZTag(97, 4.0, "", "电瓶车", "Walk", "out", "WeChat Pay", 2022, 5, 2),
            MZTag(98, 23.0, "", "Dinner 沙县", "Food", "out", "WeChat Pay", 2022, 5, 2),
        )

        val LIST = LISTs.reversed()
    }

    var page: Int = 1
    private val dao = db.mzTagDao()
    protected val constantDao: ConstantDao = db.constantDao()
    protected var isFinish = false

    protected val KEY_FINISH = KEY + "KEY_FINISH"

    /*refresh after 1 day*/
    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(minuteDuration, TimeUnit.MINUTES)

        val lastUploadTime = constantDao.lastUpload(KEY)
        val isFirst = lastUploadTime == null

        if (isFirst) {
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }

        val a = System.currentTimeMillis() - lastUploadTime!!

        return when {
            a < cacheTimeout -> {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
            else -> {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MZTag>
    ): MediatorResult {


        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    page = 1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    page = constantDao.get(KEY).value as Int
                }
            }


            val list = getMZTag(page-1, state.config.pageSize)
            isFinish = list.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clear()
                }
                dao.insert(list)
                if (!isFinish)
                    constantDao.insert(ConstantKey(KEY, ++page))

                constantDao.insert(ConstantKey(KEY_FINISH, isFinish))

            }

            return MediatorResult.Success(isFinish)
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(Throwable("error"))
        }
    }

    private fun getMZTag(page: Int, pageSize: Int): List<MZTag> {
        val size = LIST.size
        val end = if(page * pageSize > size){
            return emptyList()
        } else if ((page + 1) * pageSize > size) {
            size
        } else {
            (page + 1) * pageSize
        }

        val ret = LIST.subList(page * pageSize, end)
        if (page > 5)
            println(page)
        return ret
    }
}