package com.example.tryadmin2.view.viewmodel.book.update

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
class UpdateBookViewModel @Inject constructor(private val repository: AdminRepository) : ViewModel() {

    private var idBookResponse: DataBookResponse.BookResponse? = null

    private var putBookResponse: DataBookResponse.BookResponse? = null

    private var _idDataBookResponse = MutableLiveData<RequestState<DataBookResponse.BookResponse?>>()
    var idDataBookResponse: LiveData<RequestState<DataBookResponse.BookResponse?>> = _idDataBookResponse

    private var _putDataBookesponse = MutableLiveData<RequestState<DataBookResponse.BookResponse?>>()
    var putDataBookResponse: LiveData<RequestState<DataBookResponse.BookResponse?>> = _putDataBookesponse

    fun getDetailBook(idBook: Int) {
        viewModelScope.launch {
            _idDataBookResponse.postValue(RequestState.Loading)
            val response = repository.getDetailBook(idBook)
            _idDataBookResponse.postValue(handleGetDetailBookResponse(response))
        }
    }

    fun putBook(
        id: String,
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
            _putDataBookesponse.postValue(RequestState.Loading)
            val response = repository.putBook(
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
            _putDataBookesponse.postValue(handlePutBookResponse(response))
        }
    }

    private fun handleGetDetailBookResponse(response: Response<DataBookResponse.BookResponse>):
            RequestState<DataBookResponse.BookResponse?> {
        return if (response.isSuccessful) {
            idBookResponse = response.body()
            RequestState.Success(idBookResponse)
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

    private fun handlePutBookResponse(response: Response<DataBookResponse.BookResponse>):
            RequestState<DataBookResponse.BookResponse?> {
        return if (response.isSuccessful) {
            putBookResponse = response.body()
            RequestState.Success(putBookResponse)
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