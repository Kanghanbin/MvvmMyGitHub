package com.khb.mvvmmygithub.requestbean

import com.google.gson.annotations.SerializedName
import com.khb.mvvmmygithub.BuildConfig

/**
 *创建时间：2019/11/26
 *编写人：kanghb
 *功能描述：
 */
data class UserTokenRequest(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String,
    val note: String,
    val scopes: List<String>
){
    companion object{
        fun generate() :UserTokenRequest{
            return UserTokenRequest(
                scopes = listOf("user","repo","gist","notifications"),
                note = BuildConfig.APPLICATION_ID,
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET
            )
        }
    }
}