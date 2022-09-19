package com.example.siatlet.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.adapter.UserAdapter
import com.example.siatlet.databinding.ActivityUserBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.DataItemUser
import com.example.siatlet.model.UserResponse
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {
    private lateinit var token: String

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPref()
        setToolbar()
        setListener()
        setList()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun setListener() {
        binding.apply {
            fabAddUser.setOnClickListener {
                val intent = Intent(this@UserActivity, AddUserActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().getAllUser(token)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val responseBody = response.body()
                        val userAdapter = UserAdapter(responseBody?.data as ArrayList<DataItemUser>, this@UserActivity)
                        binding.rvUser.adapter = userAdapter
                        binding.rvUser.setHasFixedSize(true)
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setLoading(condition: Boolean) {
        if (condition) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "User"
    }
}