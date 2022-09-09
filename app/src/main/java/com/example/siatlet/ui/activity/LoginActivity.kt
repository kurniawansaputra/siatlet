package com.example.siatlet.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.siatlet.databinding.ActivityLoginBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.LoginResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
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
            username = binding.editUsername.text.toString()
            password = binding.editPassword.text.toString()

            dialog.showProgressDialog(this)
            val client = ApiConfig.getApiService().login(username, password)
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    dialog.hideDialog()
                    val statusCode = response.body()?.meta?.code
                    val message = response.body()?.meta?.message

                    if (response.isSuccessful) {
                        if (statusCode == "200") {
                            val user = response.body()
                            if (user != null) {
                                HawkStorage.instance(this@LoginActivity).setUser(user)
                                goToMain()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "$message", Toast.LENGTH_SHORT).show()
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

    private fun goToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "Login"
    }
}