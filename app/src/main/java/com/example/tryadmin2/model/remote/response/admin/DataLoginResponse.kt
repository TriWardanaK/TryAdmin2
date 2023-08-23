package com.example.tryadmin2.model.remote.response.admin

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataLoginResponse(
    @SerializedName("id_admin")
    val idAdmin: Int?,
    @SerializedName("username_admin")
    val usernameAdmin: String?,
    @SerializedName("password_admin")
    val passwordAdmin: String?,
) : Parcelable