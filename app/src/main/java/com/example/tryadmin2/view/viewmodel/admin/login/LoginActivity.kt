package com.example.tryadmin2.view.viewmodel.admin.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tryadmin2.R
import com.example.tryadmin2.view.viewmodel.admin.main.MainActivity
import com.example.tryadmin2.databinding.ActivityLoginBinding
import com.example.tryadmin2.util.RequestState
import com.example.tryadmin2.view.viewmodel.admin.signup.SignupActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            postLoginAdmin()
        }

        binding.btnSignup.setOnClickListener {
            btnSignup()
        }

        dataLoginResponse()
    }

    private fun postLoginAdmin() {
        val usernameAdmin = binding.edtUsernameAdmin.text.toString()
        val passwordAdmin = binding.edtPasswordAdmin.text.toString()
        when {
            usernameAdmin.isEmpty() -> {
                binding.edtUsernameAdmin.error = getString(R.string.masih_kosong)
            }
            passwordAdmin.isEmpty() -> {
                binding.edtPasswordAdmin.error = getString(R.string.masih_kosong)
            }
            else -> {
                viewModel.postLoginAdmin(
                    usernameAdmin,
                    passwordAdmin
                )
            }
        }
    }

    private fun dataLoginResponse() {
        viewModel.dataLoginResponse.observe(this) {
            when (it) {
                is RequestState.Loading -> showLoading()
                is RequestState.Success -> {
                    hideLoading()
                    if (it.data?.idAdmin != null) {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.login_berhasil) ,
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("idAdmin", it.data.idAdmin)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.login_gagal),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is RequestState.Error -> {
                    hideLoading()
                    Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showLoading() {
        binding.pbLogin.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbLogin.visibility = View.GONE
    }

    private fun btnSignup() {
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        startActivity(intent)
    }
}