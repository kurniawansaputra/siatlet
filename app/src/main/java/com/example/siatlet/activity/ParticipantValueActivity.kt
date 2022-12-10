package com.example.siatlet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.adapter.ParticipantValueAdapter
import com.example.siatlet.databinding.ActivityParticipantValueBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.DataParticipantValue
import com.example.siatlet.model.DataParticipantValueByIdResponse
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParticipantValueActivity : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var nameContest: String
    private lateinit var name: String
    private lateinit var idParticipant: String

    private lateinit var binding: ActivityParticipantValueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantValueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPref()
        setToolbar()
        setList()
        setListener()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idContest = intent.getStringExtra("id_contest").toString()
        nameContest = intent.getStringExtra("name_contest").toString()
        name = intent.getStringExtra("name_participant").toString()
        idParticipant = intent.getStringExtra("id_participant").toString()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            toolbar.title = "Daftar Nilai Peserta - $name"
            toolbar.subtitle = nameContest
        }
    }

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().getDataParticipantValueById(token, idParticipant)
        client.enqueue(object : Callback<DataParticipantValueByIdResponse> {
            override fun onResponse(call: Call<DataParticipantValueByIdResponse>, response: Response<DataParticipantValueByIdResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val responseBody = response.body()
                        val participantValueAdapter = ParticipantValueAdapter(responseBody?.data as ArrayList<DataParticipantValue>, this@ParticipantValueActivity)
                        binding.rvParticipantValue.adapter = participantValueAdapter
                        binding.rvParticipantValue.setHasFixedSize(true)
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DataParticipantValueByIdResponse>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setListener() {
        binding.apply {
            fabAddParticipantValue.setOnClickListener {
                val intent = Intent(this@ParticipantValueActivity, AddParticipantValueActivity::class.java)
                intent.putExtra("id_contest", idContest)
                intent.putExtra("name_contest", nameContest)
                intent.putExtra("id_participant", idParticipant)
                intent.putExtra("name_participant", name)
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
        const val TAG = "ParticipantValue"
    }
}