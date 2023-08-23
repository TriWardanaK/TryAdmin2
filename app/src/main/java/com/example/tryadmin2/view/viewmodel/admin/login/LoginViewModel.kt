package com.example.tryadmin2.view.viewmodel.admin.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryadmin2.model.remote.response.admin.DataLoginResponse
import com.example.tryadmin2.repository.AdminRepository
import com.example.tryadmin2.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AdminRepository) : ViewModel() {

    private var loginResponse: DataLoginResponse? = null

    private var _dataLoginResponse = MutableLiveData<RequestState<DataLoginResponse?>>()
    var dataLoginResponse: LiveData<RequestState<DataLoginResponse?>> = _dataLoginResponse

    fun postLoginAdmin(username: String, password: String) {
        viewModelScope.launch {
            _dataLoginResponse.postValue(RequestState.Loading)
            val response = repository.postLoginAdmin(username, password)
            _dataLoginResponse.postValue(handlePostLoginAdminResponse(response))
        }
    }

    private fun handlePostLoginAdminResponse(response: Response<DataLoginResponse>):
            RequestState<DataLoginResponse?> {
        return if (response.isSuccessful) {
            loginResponse = response.body()
            RequestState.Success(loginResponse)
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