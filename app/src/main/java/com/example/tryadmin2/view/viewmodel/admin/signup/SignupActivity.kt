package com.example.tryadmin2.view.viewmodel.admin.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tryadmin2.R
import com.example.tryadmin2.databinding.ActivitySignupBinding
import com.example.tryadmin2.util.RequestState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            postAdmin()
        }
        dataAdminResponse()
    }

    private fun postAdmin() {
        binding.apply {
            val usernameAdmin = edtUsernameAdmin.text.toString()
            val passwordAdmin = edtPasswordAdmin.text.toString()
            val namaAdmin = edtNamaAdmin.text.toString()
            val telpAdmin = edtTelpAdmin.text.toString()
            val alamatAdmin = edtAlamatAdmin.text.toString()
            when {
                usernameAdmin.isEmpty() -> {
                    edtUsernameAdmin.error = getString(R.string.masih_kosong)
                }
                passwordAdmin.isEmpty() -> {
                    edtPasswordAdmin.error = getString(R.string.masih_kosong)
                }
                namaAdmin.isEmpty() -> {
                    edtNamaAdmin.error = getString(R.string.masih_kosong)
                }
                telpAdmin.isEmpty() -> {
                    edtTelpAdmin.error = getString(R.string.masih_kosong)
                }
                alamatAdmin.isEmpty() -> {
                    edtAlamatAdmin.error = getString(R.string.masih_kosong)
                }
                else -> {
                    viewModel.postAdmin(
                        usernameAdmin,
                        passwordAdmin,
                        namaAdmin,
                        telpAdmin,
                        alamatAdmin
                    )
                }
            }
        }
    }

    private fun dataAdminResponse() {
        viewModel.dataAdminResponse.observe(this) {
            when (it) {
                is RequestState.Loading -> showLoading()
                is RequestState.Success -> {
                    hideLoading()
                    Toast.makeText(this@SignupActivity, getString(R.string.data_berhasil_disimpan), Toast.LENGTH_SHORT).show()
                    binding.apply {
                        edtUsernameAdmin.text.clear()
                        edtPasswordAdmin.text.clear()
                        edtNamaAdmin.text.clear()
                        edtTelpAdmin.text.clear()
                        edtAlamatAdmin.text.clear()
                    }
                }
                is RequestState.Error -> {
                    hideLoading()
                    Toast.makeText(this@SignupActivity, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showLoading() {
        binding.pbSignup.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbSignup.visibility = View.GONE
    }
}