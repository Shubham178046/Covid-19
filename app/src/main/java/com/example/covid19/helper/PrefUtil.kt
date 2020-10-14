package com.example.covid19.helper

import android.content.Context

object PrefUtil {
    var IS_LOGIN = "islogin"
    var USER_NEW_OR_EXISTING ="user"
    var USER_NAME = "name"
    var USER_SURNAME = "surname"
    var USER_PROFILE_IMAGE = "image"
    fun setLoggedIn(ctx: Context?, value: Boolean) {
        Prefs.with(ctx!!).save(IS_LOGIN, value)
    }

    fun isUserLoggedIn(ctx: Context?): Boolean {
        return Prefs.with(ctx!!).getBoolean(IS_LOGIN, false)
    }

    fun setUser(ctx: Context?, value: Boolean) {
        Prefs.with(ctx!!).save(USER_NEW_OR_EXISTING, value)
    }

    fun isAlreadyExisting(ctx: Context?): Boolean {
        return Prefs.with(ctx!!).getBoolean(USER_NEW_OR_EXISTING, false)
    }


    fun setUserName(ctx : Context , value : String)
    {
        Prefs.with(ctx).save(USER_NAME,value)
    }

    fun getUserName(ctx: Context) : String?
    {
        return Prefs.with(ctx).getString(USER_NAME,"")
    }

    fun setUserSurname(ctx : Context , value : String)
    {
        Prefs.with(ctx).save(USER_SURNAME,value)
    }

    fun getUserSurname(ctx: Context) : String?
    {
        return Prefs.with(ctx).getString(USER_SURNAME,"")
    }

    fun setUserProfile(ctx: Context , value : String)
    {
        Prefs.with(ctx).save(USER_PROFILE_IMAGE,value)
    }

    fun getUserProfile(ctx: Context) : String?
    {
        return Prefs.with(ctx).getString(USER_PROFILE_IMAGE,"")
    }
}