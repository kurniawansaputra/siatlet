package com.example.siatlet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.adapter.CriteriaWeightAdapter
import com.example.siatlet.databinding.ActivityCriteriaWeightBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.CriteriaWeightByContestResponse
import com.example.siatlet.model.DataCriteriaWeightByContest
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CriteriaWeightActivity : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var nameContest: String

    private lateinit var binding: ActivityCriteriaWeightBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriteriaWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPref()
        setToolbar()
        setListener()
        setList()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idContest = intent.getStringExtra("id_contest").toString()
        nameContest = intent.getStringExtra("name_contest").toString()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            toolbar.subtitle = nameContest
        }
    }

    private fun setListener() {
        binding.apply {
            fabAddCriteria.setOnClickListener {
                val intent = Intent(this@CriteriaWeightActivity, AddCriteriaWeightActivity::class.java)
                intent.putExtra("id_contest", idContest)
                intent.putExtra("name_contest", nameContest)
                startActivity(intent)
            }
        }
    }

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().getCriteriaWeihgtByContest(token, idContest)
        client.enqueue(object : Callback<CriteriaWeightByContestResponse> {
            override fun onResponse(call: Call<CriteriaWeightByContestResponse>, response: Response<CriteriaWeightByContestResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val responseBody = response.body()
                        val criteriaWeightAdapter = CriteriaWeightAdapter(responseBody?.data as ArrayList<DataCriteriaWeightByContest>, this@CriteriaWeightActivity)
                        binding.rvCriteriaWeight.adapter = criteriaWeightAdapter
                        binding.rvCriteriaWeight.setHasFixedSize(true)
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaWeightByContestResponse>, t: Throwable) {
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
        const val TAG = "CriteriaWeight"
    }
}