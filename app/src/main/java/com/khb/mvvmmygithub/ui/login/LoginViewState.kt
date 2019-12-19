package com.khb.mvvmmygithub.ui.login

import com.khb.mvvmmygithub.responseentity.UserInfo

/**
 *创建时间：2019/12/4
 *编写人：kanghb
 *功能描述：
 */
data class LoginViewState(
    val isLoading: Boolean,
    val throwable: Throwable?,
    val loginInfo: UserInfo?,
    val autoLoginEvent: AutoLoginEvent?,
    val useAutoLogin: Boolean

) {
    companion object {
        fun initial(): LoginViewState {
            return LoginViewState(
                false,
                null,
                null,
                null,
                true
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginViewState
        if (isLoading != other.isLoading) return false
        if (throwable != other.throwable) return false
        if (loginInfo != other.loginInfo) return false
        if (autoLoginEvent != other.autoLoginEvent) return false
        if (useAutoLogin != other.useAutoLogin) return false
        return true
    }

    override fun hashCode(): Int {

        var result = isLoading.hashCode()
        result = 31 * result + (throwable?.hashCode() ?: 0)
        result = 31 * result + (loginInfo?.hashCode() ?: 0)
        result = 31 * result + (autoLoginEvent?.hashCode() ?: 0)
        result = 31 * result + useAutoLogin.hashCode()
        return result
    }
}