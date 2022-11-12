package com.example.siatlet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.adapter.CriteriaValueAdapter
import com.example.siatlet.databinding.ActivityCriteriaValueBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.CriteriaValueByIdCriteriaResponse
import com.example.siatlet.model.DataCriteriaValue
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CriteriaValueActivity : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var idCriteria: String
    private lateinit var nameCriteria: String
    private lateinit var nameContest: String

    private lateinit var binding: ActivityCriteriaValueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriteriaValueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPref()
        setToolbar()
        setList()
        setListener()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idCriteria = intent.getStringExtra("id_criteria").toString()
        nameCriteria = intent.getStringExtra("name_criteria").toString()
        nameContest = intent.getStringExtra("name_contest").toString()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            toolbar.title = nameCriteria
            toolbar.subtitle = nameContest
        }
    }

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().getCriteriaValueByIdCriteria(token, idCriteria)
        client.enqueue(object : Callback<CriteriaValueByIdCriteriaResponse> {
            override fun onResponse(call: Call<CriteriaValueByIdCriteriaResponse>, response: Response<CriteriaValueByIdCriteriaResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val responseBody = response.body()
                        val criteriaValueAdapter = CriteriaValueAdapter(responseBody?.data as ArrayList<DataCriteriaValue>, this@CriteriaValueActivity)
                        binding.rvCriteriaValue.adapter = criteriaValueAdapter
                        binding.rvCriteriaValue.setHasFixedSize(true)
                    }

                } else {
                    Log.e(CriteriaByContestActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaValueByIdCriteriaResponse>, t: Throwable) {
                setLoading(false)
                Log.e(CriteriaByContestActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setListener() {
        binding.apply {
            fabAddCriteriaValue.setOnClickListener {
                val intent = Intent(this@CriteriaValueActivity, AddCriteriaValueActivity::class.java)
                intent.putExtra("id_criteria", idCriteria)
                intent.putExtra("name_criteria", nameCriteria)
                intent.putExtra("name_contest", nameContest)
                startActivity(intent)
            }
        }
    }

    private fun setLoading(condition: Boolean) {
        if (condition) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "CriteriaValue"
    }
}