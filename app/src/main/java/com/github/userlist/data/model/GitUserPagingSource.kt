package com.github.userlist.data.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.userlist.data.api.ApiService
import com.github.userlist.data.constant.Constants.DEFAULT.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class GithubPagingSource(
    private val service: ApiService
) : PagingSource<Int, GitUser>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitUser> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {

            val response = service.getUsers(position, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = response.lastOrNull()?.id
            )

        } catch (exception: IOException) {
            Log.e("excep", "IO")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.e("excep", "${exception.code()}")
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GitUser>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


}
