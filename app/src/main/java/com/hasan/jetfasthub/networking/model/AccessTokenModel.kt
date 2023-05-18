package com.hasan.jetfasthub.networking.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class AccessTokenModel private constructor(parcel: Parcel) : Parcelable {
    private val id: Long
    private val token: String?
    private val hashedToken: String?
    private val accessToken: String?
    private val tokenType: String?
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(token)
        dest.writeString(hashedToken)
        dest.writeString(accessToken)
        dest.writeString(tokenType)
    }

    init {
        id = parcel.readLong()
        token = parcel.readString()
        hashedToken = parcel.readString()
        accessToken = parcel.readString()
        tokenType = parcel.readString()
    }

    companion object CREATOR : Creator<AccessTokenModel> {
        override fun createFromParcel(parcel: Parcel): AccessTokenModel {
            return AccessTokenModel(parcel)
        }

        override fun newArray(size: Int): Array<AccessTokenModel?> {
            return arrayOfNulls(size)
        }
    }

}