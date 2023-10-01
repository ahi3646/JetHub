package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.core.ui.utils.Constants

class PreferenceHelper (val context: Context){
    fun saveToken(token:String){
        val sharedPref = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(Constants.TOKEN_KEY, token)
            apply()
        }
    }
    fun saveAuthenticatedUser(username: String){
        val sharedPref = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(Constants.USERNAME_KEY, username)
            apply()
        }
    }
    fun getToken(): String {
        val sharedPref = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPref.getString(Constants.TOKEN_KEY, "")!!
    }
    fun getAuthenticatedUsername(): String {
        val sharedPref = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPref.getString(Constants.USERNAME_KEY, "")!!
    }
}