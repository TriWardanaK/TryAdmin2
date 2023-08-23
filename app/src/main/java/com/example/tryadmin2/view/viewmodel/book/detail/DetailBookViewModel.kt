package com.example.tryadmin2.view.viewmodel.book.detail

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
class DetailBookViewModel @Inject constructor(private val repository: AdminRepository) : ViewModel() {

    private var bookResponse: DataBookResponse.BookResponse? = null

    private var _dataBookResponse = MutableLiveData<RequestState<DataBookResponse.BookResponse?>>()
    var dataBookResponse: LiveData<RequestState<DataBookResponse.BookResponse?>> = _dataBookResponse

    fun getDetailBook(id: Int?) {
        viewModelScope.launch {
            _dataBookResponse.postValue(RequestState.Loading)
            val response = repository.getDetailBook(id)
            _dataBookResponse.postValue(handleGetDetailBookResponse(response))
        }
    }

    private fun handleGetDetailBookResponse(response: Response<DataBookResponse.BookResponse>):
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