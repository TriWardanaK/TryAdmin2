package com.example.tryadmin2.model.remote.response.admin

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataAdminResponse(
    val data: ArrayList<AdminResponse>?
) : Parcelable {
    @Parcelize
    data class AdminResponse(
        @SerializedName("id_admin")
        val idAdmin: Int?,
        @SerializedName("username_admin")
        val usernameAdmin: String?,
        @SerializedName("password_admin")
        val passwordAdmin: String?,
        @SerializedName("nama_admin")
        val namaAdmin: String?,
        @SerializedName("no_telp_admin")
        val telpAdmin: String?,
        @SerializedName("alamat_admin")
        val alamatAdmin: String?,
    ) : Parcelable
}