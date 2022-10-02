package com.example.siatlet.ui.activity

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
    private lateinit var level: String

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
        level = user.data?.level.toString()
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
            if (level == "pelatih") {
                fabAddParticipant.visibility = View.VISIBLE
            } else {
                fabAddParticipant.visibility = View.GONE
            }
        }
    }

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().getAllParticipant(token)
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