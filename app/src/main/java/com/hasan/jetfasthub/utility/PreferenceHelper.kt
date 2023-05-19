package com.hasan.jetfasthub.utility

import android.content.Context

private const val SHARED_PREF = "jetfasthub_pref"
private const val TOKEN_KEY = "jethub_pref_key"

object PreferenceHelper {

    fun saveToken(context: Context, token:String){
        val sharedPref = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getToken(context: Context): String {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, "")!!
    }

}