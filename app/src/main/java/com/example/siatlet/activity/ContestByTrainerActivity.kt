package com.example.siatlet.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.adapter.ContestByTrainerAdapter
import com.example.siatlet.databinding.ActivityContestByTrainerBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.ContestResponse
import com.example.siatlet.model.DataContest
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContestByTrainerActivity : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var idTrainer: String

    private lateinit var binding: ActivityContestByTrainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContestByTrainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPref()
        setToolbar()
        setListener()
        setList()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()
        idTrainer = user.data?.idUser.toString()
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
//            if (level == "pelatih") {
//                fabAddParticipant.visibility = View.VISIBLE
//            } else {
//                fabAddParticipant.visibility = View.GONE
//            }
        }
    }

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().getContestByTrainer(idTrainer, token)
        client.enqueue(object : Callback<ContestResponse> {
            override fun onResponse(call: Call<ContestResponse>, response: Response<ContestResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val responseBody = response.body()
                        val contestByTrainerAdapter = ContestByTrainerAdapter(responseBody?.data as ArrayList<DataContest>, this@ContestByTrainerActivity)
                        binding.rvContest.adapter = contestByTrainerAdapter
                        binding.rvContest.setHasFixedSize(true)
                    }

                } else {
                    Log.e(UserActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ContestResponse>, t: Throwable) {
                setLoading(false)
                Log.e(UserActivity.TAG, "onFailure: ${t.message}")
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
        const val TAG = "Participant"
    }
}