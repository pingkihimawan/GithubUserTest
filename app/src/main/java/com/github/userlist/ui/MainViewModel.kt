package com.github.userlist.ui

import androidx.lifecycle.*
import androidx.paging.*
import com.github.userlist.data.api.ApiService
import com.github.userlist.data.model.GitUser
import com.github.userlist.data.model.GitUserDetail
import com.github.userlist.data.model.GithubRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel(), LifecycleObserver {

    fun getUsers(): Flow<PagingData<GitUser>> =
        repository.getResultStream()
            .cachedIn(viewModelScope)

    suspend fun getUserDetail(userName:String): GitUserDetail = repository.getUserDetail(userName)

}