package com.khb.mvvmmygithub.responseentity

/**
 *创建时间：2019/11/26
 *编写人：kanghb
 *功能描述：
 */
data class UserAcessToken(
    val app: App,
    val created_at: String,
    val fingerprint: Any,
    val hashed_token: String,
    val id: Int,
    val note: String,
    val note_url: Any,
    val scopes: List<String>,
    val token: String,
    val token_last_eight: String,
    val updated_at: String,
    val url: String
)

data class App(
    val client_id: String,
    val name: String,
    val url: String
)