package com.atom.heartratecompose.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.atom.heartratecompose.db.entity.HeartRateEntity
import com.atom.heartratecompose.repo.AppRepo
import com.atom.heartratecompose.utils.logE

/**
 * Author: 芒硝
 * Email : 1248389474@qq.com
 * Date  : 2021/8/19 10:23
 * Desc  : data store
 */
class HeartRateListDataSource(
    private val repo: AppRepo
): PagingSource<Int, HeartRateEntity>() {
    override fun getRefreshKey(state: PagingState<Int, HeartRateEntity>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HeartRateEntity> {
        val loadPage = params.key ?: 0
        val data = repo.queryHeartRate(offset = loadPage)
        val nextPage = if (data?.size!! < 10) null else loadPage + 1
        "===HeartRateListDataSource load=====${data}".logE()
        return if (data != null){
            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextPage
            )
        } else {
            LoadResult.Error(throwable = Throwable())
        }
    }
}