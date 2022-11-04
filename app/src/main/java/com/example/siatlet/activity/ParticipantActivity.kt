package com.example.siatlet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.adapter.ParticipantAdapter
import com.example.siatlet.databinding.ActivityParticipantBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.DataParticipant
import com.example.siatlet.model.ParticipantResponse
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParticipantActivity : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var nameContest: String

    private lateinit var binding: ActivityParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantBinding.inflate(layoutInflater)
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
            fabAddParticipant.setOnClickListener {
                val intent = Intent(this@ParticipantActivity, AddParticipantActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().getParticipantByIdContest(token, idContest)
        client.enqueue(object : Callback<ParticipantResponse> {
            override fun onResponse(call: Call<ParticipantResponse>, response: Response<ParticipantResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val responseBody = response.body()
                        val participantAdapter = ParticipantAdapter(responseBody?.data as ArrayList<DataParticipant>, this@ParticipantActivity)
                        binding.rvParticipant.adapter = participantAdapter
                        binding.rvParticipant.setHasFixedSize(true)
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ParticipantResponse>, t: Throwable) {
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
        const val TAG = "Participant"
    }
}