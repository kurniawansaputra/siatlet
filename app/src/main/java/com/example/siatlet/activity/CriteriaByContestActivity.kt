package com.example.siatlet.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.adapter.CriteriaByContestAdapter
import com.example.siatlet.databinding.ActivityCriteriaByContestBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.CriteriaResponse
import com.example.siatlet.model.DataCriteria
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CriteriaByContestActivity : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var nameContest: String

    private lateinit var binding: ActivityCriteriaByContestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriteriaByContestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPref()
        setToolbar()
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

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().getCriteriaByIdContest(token, idContest)
        client.enqueue(object : Callback<CriteriaResponse> {
            override fun onResponse(call: Call<CriteriaResponse>, response: Response<CriteriaResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val responseBody = response.body()
                        val criteriaByContestAdapter = CriteriaByContestAdapter(responseBody?.data as ArrayList<DataCriteria>, this@CriteriaByContestActivity)
                        binding.rvCriteria.adapter = criteriaByContestAdapter
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