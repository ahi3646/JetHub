package com.hasan.jetfasthub.data

import android.content.Context
import android.content.SharedPreferences
import com.hasan.jetfasthub.core.ui.utils.Constants

class PreferenceHelper (val context: Context){
    private val sharedPref: SharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)

    fun saveToken(token:String){
        with(sharedPref.edit()) {
            putString(Constants.TOKEN_KEY, "Bearer $token")
            apply()
        }
    }

    fun isLogged(): Boolean{
        return getToken().isNotEmpty()
    }

    fun saveAuthenticatedUser(username: String){
        with(sharedPref.edit()) {
            putString(Constants.USERNAME_KEY, username)
            apply()
        }
    }
    fun getToken(): String {
        return sharedPref.getString(Constants.TOKEN_KEY, "")!!
    }
    fun getAuthenticatedUsername(): String {
        return sharedPref.getString(Constants.USERNAME_KEY, "")!!
    }
}