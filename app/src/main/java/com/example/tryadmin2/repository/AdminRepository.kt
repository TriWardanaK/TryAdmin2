package com.example.tryadmin2.repository

import com.example.tryadmin2.model.remote.ApiService
import com.example.tryadmin2.model.remote.response.admin.DataAdminResponse
import com.example.tryadmin2.model.remote.response.admin.DataLoginResponse
import com.example.tryadmin2.model.remote.response.book.DataBookResponse
import retrofit2.Response
import javax.inject.Inject

class AdminRepository @Inject constructor(private val apiService: ApiService) {
    //admin
    suspend fun postAdmin(
        username: String?,
        password: String?,
        nama: String?,
        telp: String?,
        alamat: String?
    ): Response<DataAdminResponse.AdminResponse> =
        apiService.postAdmin(username, password, nama, telp, alamat)

    suspend fun postLoginAdmin(username: String?, password: String?): Response<DataLoginResponse> =
        apiService.postLoginAdmin(username, password)

    suspend fun getDetailAdmin(id: Int?): Response<DataAdminResponse.AdminResponse> =
        apiService.getDetailAdmin(id)

    suspend fun putAdmin(
        id: Int?,
        username: String?,
        password: String?,
        nama: String?,
        telp: String?,
        alamat: String?
    ): Response<DataAdminResponse.AdminResponse> =
        apiService.putAdmin(id, username, password, nama, telp, alamat)

    //book
    suspend fun postBook(
        judul: String?,
        penulis: String?,
        penerbit: String?,
        tahunTerbit: Int?,
        jumlahHalaman: Int?,
        stok: Int?,
        cover: String?,
        deskripsi: String?
    ): Response<DataBookResponse.BookResponse> =
        apiService.postBook(
            judul,
            penulis,
            penerbit,
            tahunTerbit,
            jumlahHalaman,
            stok,
            cover,
            deskripsi
        )

    suspend fun putBook(
        id: String?,
        judul: String?,
        penulis: String?,
        penerbit: String?,
        tahunTerbit: Int?,
        jumlahHalaman: Int?,
        stok: Int?,
        cover: String?,
        deskripsi: String?
    ): Response<DataBookResponse.BookResponse> =
        apiService.putBook(
            id,
            judul,
            penulis,
            penerbit,
            tahunTerbit,
            jumlahHalaman,
            stok,
            cover,
            deskripsi
        )

    suspend fun getBook(): Response<DataBookResponse> = apiService.getBook()

    suspend fun getDetailBook(id: Int?): Response<DataBookResponse.BookResponse> =
        apiService.getDetailBook(id)

    suspend fun deleteBook(id: Int?): Response<DataBookResponse.BookResponse> = apiService.deleteBook(id)
}