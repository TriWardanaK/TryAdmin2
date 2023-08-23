package com.example.tryadmin2.view.viewmodel.admin.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryadmin2.model.remote.response.admin.DataAdminResponse
import com.example.tryadmin2.repository.AdminRepository
import com.example.tryadmin2.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val repository: AdminRepository) : ViewModel() {

    private var adminResponse: DataAdminResponse.AdminResponse? = null

    private var _dataAdminResponse = MutableLiveData<RequestState<DataAdminResponse.AdminResponse?>>()
    var dataAdminResponse: LiveData<RequestState<DataAdminResponse.AdminResponse?>> = _dataAdminResponse

    fun postAdmin(username: String, password: String, nama: String, telp: String, alamat: String) {
        viewModelScope.launch {
            _dataAdminResponse.postValue(RequestState.Loading)
            val response = repository.postAdmin(username, password, nama, telp, alamat)
            _dataAdminResponse.postValue(handlePostAdminResponse(response))
        }
    }

    private fun handlePostAdminResponse(response: Response<DataAdminResponse.AdminResponse>):
            RequestState<DataAdminResponse.AdminResponse?> {
        return if (response.isSuccessful) {
            adminResponse = response.body()
            RequestState.Success(adminResponse)
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