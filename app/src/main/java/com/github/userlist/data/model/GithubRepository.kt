package com.github.userlist.data.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.userlist.data.api.ApiService
import com.github.userlist.data.constant.Constants.DEFAULT.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class GithubRepository(private val service: ApiService) {

    fun getResultStream(): Flow<PagingData<GitUser>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false, initialLoadSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { GithubPagingSource(service) }
        ).flow
    }

    suspend fun getUserDetail(userName: String): GitUserDetail {
        return service.getUserDetail(userName)
    }

}
