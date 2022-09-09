package com.example.siatlet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.siatlet.databinding.ActivityLoginBinding
import com.example.siatlet.model.LoginResponse
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var username: String
    private lateinit var password: String

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fillForm()
        postLogin()
    }

    private fun fillForm() {
        binding.editUsername.setText("nining1")
        binding.editPassword.setText("123456")
    }

    private fun postLogin() {
        binding.buttonLogin.setOnClickListener {
            username = binding.editUsername.text.toString()
            password = binding.editPassword.text.toString()

//            showLoading(true)
            val client = ApiConfig.getApiService().getLogin(username, password)
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                    showLoading(false)
                    if (response.isSuccessful) {
                        val statusCode = response.body()?.code
                        val data = response.body()?.message

                        if (statusCode == "200") {
                            val token = data?.token

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                            Toast.makeText(this@LoginActivity, "token $token", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, "Akun tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                    showLoading(false)
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    companion object {
        const val TAG = "Login"
    }
}