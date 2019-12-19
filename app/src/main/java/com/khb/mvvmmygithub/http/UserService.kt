package com.khb.mvvmmygithub.http

import com.khb.mvvmmygithub.requestbean.UserTokenRequest
import com.khb.mvvmmygithub.responseentity.ReceivedEvent
import com.khb.mvvmmygithub.responseentity.Repos
import com.khb.mvvmmygithub.responseentity.UserAcessToken
import com.khb.mvvmmygithub.responseentity.UserInfo
import io.reactivex.Flowable
import retrofit2.http.*

/**
 *创建时间：2019/11/11
 *编写人：kanghb
 *功能描述：
 */
interface UserService {

    @GET("user")
    fun fenchUserOwner(): Flowable<UserInfo>

    @GET("users/{username}/received_events?")
    fun queryReceivedEvents(
        @Path("username") username: String,
        @Query("page") pageIndex: Int,
        @Query("per_page") perPage: Int
    ): Flowable<List<ReceivedEvent>>


    //发邮件要弃用改api了，详情移步https://developer.github.com/changes/2019-11-05-deprecated-passwords-and-authorizations-api/
    @POST("authorizations")
    @Headers("Accept: application/json")
    fun authorizations(
        @Body userTokenRequest: UserTokenRequest
    ): Flowable<UserAcessToken>

    @GET("users/{username}/repos?")
    fun queryRepos(
        @Path("username") username: String,
        @Query("page") pageIndex: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String
    ): Flowable<List<Repos>>
}