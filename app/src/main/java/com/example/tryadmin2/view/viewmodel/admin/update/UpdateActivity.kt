package com.example.tryadmin2.view.viewmodel.admin.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.tryadmin2.R
import com.example.tryadmin2.databinding.ActivityUpdateBinding
import com.example.tryadmin2.util.RequestState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private val viewModel: UpdateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getDetailAdmin(getIdAdmin())
        getDetailAdmin()

        binding.btnUpdate.setOnClickListener {
            putAdmin()
        }
        dataAdminResponse()
    }

    private fun getIdAdmin(): Int {
        return intent.getIntExtra("idAdmin", 0)
    }

    private fun getDetailAdmin() {
        viewModel.idDataAdminResponse.observe(this) {
            when (it) {
                is RequestState.Loading -> showLoading()
                is RequestState.Success -> {
                    hideLoading()
                    binding.apply {
                        it.data?.apply {
                            edtUsernameAdmin.setText(usernameAdmin)
                            edtPasswordAdmin.setText(passwordAdmin)
                            edtNamaAdmin.setText(namaAdmin)
                            edtTelpAdmin.setText(telpAdmin)
                            edtAlamatAdmin.setText(alamatAdmin)
                        }
                    }
                }
                is RequestState.Error -> {
                    hideLoading()
                    Toast.makeText(this@UpdateActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun putAdmin() {
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
                    viewModel.putAdmin(
                        getIdAdmin(),
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
        viewModel.putDataAdminResponse.observe(this) {
            when (it) {
                is RequestState.Loading -> showLoading()
                is RequestState.Success -> {
                    hideLoading()
                    Toast.makeText(
                        this@UpdateActivity,
                        getString(R.string.data_berhasil_diupdate),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is RequestState.Error -> {
                    hideLoading()
                    Toast.makeText(this@UpdateActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading() {
        binding.pbUpdate.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbUpdate.visibility = View.GONE
    }
}