package com.khb.mvvmmygithub.http

import android.util.Base64
import com.khb.mvvmmygithub.BuildConfig
import com.khb.mvvmmygithub.repository.UserInfoRepository
import okhttp3.Interceptor
import okhttp3.Response


/**
 *创建时间：2019/12/13
 *编写人：kanghb
 *功能描述：
 */
class BasicAuthInterceptor(private val userInfoRepository: UserInfoRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val accessToken = getAuthorization()
        if(!accessToken.isEmpty()){
            val url = request.url.toString()
            request = request.newBuilder()
                .addHeader("Authorization",accessToken)
                .url(url)
                .build()
        }
        return chain.proceed(request)

    }

    private fun getAuthorization(): String {
//        val accessToken = userInfoRepository.accessToken
        val accessToken = BuildConfig.CLIENT_TOKEN
        val password = userInfoRepository.password
        val username = userInfoRepository.username
        if(accessToken.isBlank()){
            val basicIsEmpty = username.isBlank() || password.isBlank()
            return if(basicIsEmpty){
                ""
            }else{
                "$username:$password".let {
                    "basic " + Base64.encodeToString(it.toByteArray(),Base64.NO_WRAP)
                }
            }
        }
        return "token $accessToken"
    }

}