package com.example.siatlet.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityLoginBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.LoginResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        fillForm()
        loginToServer()
    }

    private fun init() {
        dialog = DialogUtil()
    }

    private fun fillForm() {
        binding.editUsername.setText("nining1")
        binding.editPassword.setText("123456")
    }

    private fun loginToServer() {
        binding.buttonLogin.setOnClickListener {
            username = binding.editUsername.text.toString().trim()
            password = binding.editPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                alert(R.drawable.ic_warning, "Peringatan", "Harap isi username dan password.", R.color.red)
            } else {
                dialog.showProgressDialog(this)
                val client = ApiConfig.getApiService().login(username, password)
                client.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        dialog.hideDialog()
                        val statusCode = response.body()?.meta?.code

                        if (response.isSuccessful) {
                            if (statusCode == "200") {
                                val user = response.body()
                                if (user != null) {
                                    HawkStorage.instance(this@LoginActivity).setUser(user)
                                    goToMain()
                                }
                            } else {
                                alert(R.drawable.ic_warning, "Peringatan", "Akun tidak ditemukan.", R.color.red)
                            }
                        } else {
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        dialog.hideDialog()
                        Log.e(TAG, "onFailure: ${t.message}")
                    }
                })
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "Login"
    }
}