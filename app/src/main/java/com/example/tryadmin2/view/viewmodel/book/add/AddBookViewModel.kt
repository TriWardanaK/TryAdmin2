package com.example.tryadmin2.view.viewmodel.book.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryadmin2.model.remote.response.book.DataBookResponse
import com.example.tryadmin2.repository.AdminRepository
import com.example.tryadmin2.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(private val repository: AdminRepository) : ViewModel() {

    private var bookResponse: DataBookResponse.BookResponse? = null

    private var _dataBookResponse = MutableLiveData<RequestState<DataBookResponse.BookResponse?>>()
    var dataBookResponse: LiveData<RequestState<DataBookResponse.BookResponse?>> = _dataBookResponse

    fun postBook(
        judul: String,
        penulis: String,
        penerbit: String,
        tahunTerbit: Int,
        jumlahHalaman: Int,
        stok: Int,
        cover: String,
        deskripsi: String
    ) {
        viewModelScope.launch {
            _dataBookResponse.postValue(RequestState.Loading)
            val response = repository.postBook(
                judul,
                penulis,
                penerbit,
                tahunTerbit,
                jumlahHalaman,
                stok,
                cover,
                deskripsi
            )
            _dataBookResponse.postValue(handlePostBookResponse(response))
        }
    }

    private fun handlePostBookResponse(response: Response<DataBookResponse.BookResponse>):
            RequestState<DataBookResponse.BookResponse?> {
        return if (response.isSuccessful) {
            bookResponse = response.body()
            RequestState.Success(bookResponse)
        } else RequestState.Error(
            try {
                response.errorBody()?.string()?.let {
                    JSONObject(it).get("status_message")
                }
            } catch (e: JSONException) {
                e.localizedMessage
            } as String
        )
    }
}