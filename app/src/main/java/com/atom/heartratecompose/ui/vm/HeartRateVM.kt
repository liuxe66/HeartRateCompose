package com.atom.heartratecompose.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.atom.heartratecompose.data.HeartRateListDataSource
import com.atom.heartratecompose.repo.AppRepo
import com.atom.heartratecompose.utils.logE
import kotlinx.coroutines.launch

/**
 *  author : liuxe
 *  date : 2024/1/5 17:15
 *  description :
 */
class HeartRateVM : ViewModel() {
    val repo = AppRepo()

    val pager = Pager(
        PagingConfig(
            pageSize = 10, //一次加载20条
            prefetchDistance = 5,  //距离结尾处几个时候开始加载下一页
            enablePlaceholders = true
        )
    ){
        HeartRateListDataSource(repo)

    }.flow.cachedIn(viewModelScope)

    fun queryHeartRate() = Pager(
        PagingConfig(
            pageSize = 10, //一次加载20条
            prefetchDistance = 5,  //距离结尾处几个时候开始加载下一页
            enablePlaceholders = true
        )
    ){
        HeartRateListDataSource(repo)
    }.flow.cachedIn(viewModelScope)

    /**
     * 插入
     * @param heartRate Int
     * @return Job
     */
    fun insertHeartRate(heartRate: Int) = viewModelScope.launch {
        repo.insertHeartRate(heartRate)
    }

}