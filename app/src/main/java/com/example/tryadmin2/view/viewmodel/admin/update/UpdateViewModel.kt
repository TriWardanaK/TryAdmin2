package com.example.tryadmin2.view.viewmodel.admin.update

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
class UpdateViewModel @Inject constructor(private val repository: AdminRepository) : ViewModel(){

    private var idAdminResponse: DataAdminResponse.AdminResponse? = null

    private var putAdminResponse: DataAdminResponse.AdminResponse? = null

    private var _idDataAdminResponse = MutableLiveData<RequestState<DataAdminResponse.AdminResponse?>>()
    var idDataAdminResponse: LiveData<RequestState<DataAdminResponse.AdminResponse?>> = _idDataAdminResponse

    private var _putDataAdminResponse = MutableLiveData<RequestState<DataAdminResponse.AdminResponse?>>()
    var putDataAdminResponse: LiveData<RequestState<DataAdminResponse.AdminResponse?>> = _putDataAdminResponse

    fun getDetailAdmin(idAdmin: Int) {
        viewModelScope.launch {
            _idDataAdminResponse.postValue(RequestState.Loading)
            val response = repository.getDetailAdmin(idAdmin)
            _idDataAdminResponse.postValue(handleGetDetailAdminResponse(response))
        }
    }

    fun putAdmin(id: Int, username: String, password: String, nama: String, telp: String, alamat: String) {
        viewModelScope.launch {
            _putDataAdminResponse.postValue(RequestState.Loading)
            val response = repository.putAdmin(id, username, password, nama, telp, alamat)
            _putDataAdminResponse.postValue(handlePutAdminResponse(response))
        }
    }

    private fun handleGetDetailAdminResponse(response: Response<DataAdminResponse.AdminResponse>):
            RequestState<DataAdminResponse.AdminResponse?> {
        return if (response.isSuccessful) {
            idAdminResponse = response.body()
            RequestState.Success(idAdminResponse)
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

    private fun handlePutAdminResponse(response: Response<DataAdminResponse.AdminResponse>):
            RequestState<DataAdminResponse.AdminResponse?> {
        return if (response.isSuccessful) {
            putAdminResponse = response.body()
            RequestState.Success(putAdminResponse)
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