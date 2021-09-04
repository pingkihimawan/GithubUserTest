package com.github.userlist.data.api

import com.github.userlist.data.model.GitUser
import com.github.userlist.data.model.GitUserDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): List<GitUser>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String?
    ): GitUserDetail

}