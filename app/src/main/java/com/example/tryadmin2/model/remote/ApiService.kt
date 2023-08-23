package com.example.tryadmin2.model.remote

import com.example.tryadmin2.model.remote.response.admin.DataAdminResponse
import com.example.tryadmin2.model.remote.response.admin.DataLoginResponse
import com.example.tryadmin2.model.remote.response.book.DataBookResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //admin
    @FormUrlEncoded
    @POST("admin/")
    suspend fun postAdmin(
        @Field("username_admin") username: String?,
        @Field("password_admin") password: String?,
        @Field("nama_admin") nama: String?,
        @Field("no_telp_admin") telp: String?,
        @Field("alamat_admin") alamat: String?,
    ): Response<DataAdminResponse.AdminResponse>

    @FormUrlEncoded
    @POST("login/")
    suspend fun postLoginAdmin(
        @Field("username_admin") username: String?,
        @Field("password_admin") password: String?,
    ): Response<DataLoginResponse>

    @GET("admin/{id_admin}")
    suspend fun getDetailAdmin(@Path("id_admin") id: Int?): Response<DataAdminResponse.AdminResponse>

    @FormUrlEncoded
    @PUT("admin/{id_admin}")
    suspend fun putAdmin(
        @Path("id_admin") id: Int?,
        @Field("username_admin") username: String?,
        @Field("password_admin") password: String?,
        @Field("nama_admin") nama: String?,
        @Field("no_telp_admin") telp: String?,
        @Field("alamat_admin") alamat: String?,
    ): Response<DataAdminResponse.AdminResponse>

    //book
    @GET("buku/")
    suspend fun getBook(): Response<DataBookResponse>

    @GET("buku/{id_buku}")
    suspend fun getDetailBook(@Path("id_buku") id: Int?): Response<DataBookResponse.BookResponse>

    @FormUrlEncoded
    @POST("buku/")
    suspend fun postBook(
        @Field("judul_buku") judul: String?,
        @Field("penulis_buku") penulis: String?,
        @Field("penerbit_buku") penerbit: String?,
        @Field("tahun_terbit") tahun: Int?,
        @Field("jumlah_halaman") halaman: Int?,
        @Field("stok_buku") stok: Int?,
        @Field("cover_buku") cover: String?,
        @Field("deskripsi_buku") deskripsi: String?,
    ): Response<DataBookResponse.BookResponse>

    @FormUrlEncoded
    @PUT("/buku/{id_buku}")
    suspend fun putBook(
        @Path("id_buku") id: String?,
        @Field("judul_buku") judul: String?,
        @Field("penulis_buku") penulis: String?,
        @Field("penerbit_buku") penerbit: String?,
        @Field("tahun_terbit") tahun: Int?,
        @Field("jumlah_halaman") halaman: Int?,
        @Field("stok_buku") stok: Int?,
        @Field("cover_buku") cover: String?,
        @Field("deskripsi_buku") deskripsi: String?,
    ): Response<DataBookResponse.BookResponse>

    @DELETE("buku/{id_buku}")
    suspend fun deleteBook(@Path("id_buku")id: Int?): Response<DataBookResponse.BookResponse>
}