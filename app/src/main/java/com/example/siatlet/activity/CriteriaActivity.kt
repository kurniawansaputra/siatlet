package com.example.siatlet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.adapter.CriteriaAdapter
import com.example.siatlet.databinding.ActivityCriteriaBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.CriteriaResponse
import com.example.siatlet.model.DataCriteria
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CriteriaActivity : AppCompatActivity() {
    private lateinit var token: String

    private lateinit var binding: ActivityCriteriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriteriaBinding.inflate(layoutInflater)
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
            fabAddCriteria.setOnClickListener {
                val intent = Intent(this@CriteriaActivity, AddCriteriaActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().getAllCriteria(token)
        client.enqueue(object : Callback<CriteriaResponse> {
            override fun onResponse(call: Call<CriteriaResponse>, response: Response<CriteriaResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val responseBody = response.body()
                        val criteriaAdapter = CriteriaAdapter(responseBody?.data as ArrayList<DataCriteria>, this@CriteriaActivity)
                        binding.rvCriteria.adapter = criteriaAdapter
                        binding.rvCriteria.setHasFixedSize(true)
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaResponse>, t: Throwable) {
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
        const val TAG = "Criteria"
    }
}