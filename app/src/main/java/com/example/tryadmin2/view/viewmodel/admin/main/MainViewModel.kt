package com.example.tryadmin2.view.viewmodel.admin.main

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
class MainViewModel @Inject constructor(private val repository: AdminRepository) : ViewModel() {
    private var bookResponse: DataBookResponse? = null
    private var delBookResponse: DataBookResponse.BookResponse? = null

    private var _dataBookResponse = MutableLiveData<RequestState<DataBookResponse?>>()
    var dataBookResponse: LiveData<RequestState<DataBookResponse?>> = _dataBookResponse

    private var _deleteBookResponse = MutableLiveData<RequestState<DataBookResponse.BookResponse?>>()
    var deleteBookResponse: LiveData<RequestState<DataBookResponse.BookResponse?>> = _deleteBookResponse

    fun getBook() {
        viewModelScope.launch {
            _dataBookResponse.postValue(RequestState.Loading)
            val response = repository.getBook()
            _dataBookResponse.postValue(handleGetBookResponse(response))
        }
    }

    fun deleteBook(id: Int?) {
        viewModelScope.launch {
            _deleteBookResponse.postValue(RequestState.Loading)
            val response = repository.deleteBook(id)
            _deleteBookResponse.postValue(handleDeleteBookResponse(response))
        }
    }

    private fun handleGetBookResponse(response: Response<DataBookResponse>):
            RequestState<DataBookResponse?> {
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

    private fun handleDeleteBookResponse(response: Response<DataBookResponse.BookResponse>):
            RequestState<DataBookResponse.BookResponse?> {
        return if (response.isSuccessful) {
            delBookResponse = response.body()
            RequestState.Success(delBookResponse)
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