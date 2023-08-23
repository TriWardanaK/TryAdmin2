package com.example.tryadmin2.model.remote.response.book

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataBookResponse(
    val data: ArrayList<BookResponse>?
) : Parcelable {
    @Parcelize
    data class BookResponse(
        @SerializedName("id_buku")
        val idBuku: Int?,
        @SerializedName("judul_buku")
        val judulBuku: String?,
        @SerializedName("penulis_buku")
        val penulisBuku: String?,
        @SerializedName("penerbit_buku")
        val penerbitBuku: String?,
        @SerializedName("deskripsi_buku")
        val deskripsiBuku: String?,
        @SerializedName("tahun_terbit")
        val tahunTerbit: Int?,
        @SerializedName("jumlah_halaman")
        val jumlahHalaman: Int?,
        @SerializedName("cover_buku")
        val coverBuku: String?,
        @SerializedName("stok_buku")
        val stokBuku: Int?
    ) :Parcelable
}