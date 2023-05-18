package com.hasan.jetfasthub.networking.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class AuthModel(

     private var clientId: String? = null,
     private var clientSecret: String? = null,
     private var redirectUri: String? = null,
     private var scopes: List<String>? = null,
     private var state: String? = null,
     private var note: String? = null,
     private var noteUrl: String? = null
)
