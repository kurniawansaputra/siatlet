package com.example.siatlet.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.adapter.ParticipantRankingAdapter
import com.example.siatlet.databinding.ActivityRankedBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.DataParticipantRanking
import com.example.siatlet.model.ParticipantRankingResponse
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankedActivity : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var nameContest: String
    private var gender: String = "laki-laki"

    private lateinit var binding: ActivityRankedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setGender()
        setListener()

    }

    private fun init() {
        binding.rbMale.isChecked = true
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
            buttonShow.setOnClickListener{
                setList()
            }
        }
    }

    private fun setList() {
        setLoading(true)
        val client = ApiConfig.getApiService().participantRanking(token, idContest, gender)
        client.enqueue(object : Callback<ParticipantRankingResponse> {
            override fun onResponse(call: Call<ParticipantRankingResponse>, response: Response<ParticipantRankingResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val responseBody = response.body()
                        val participantRankingAdapter = ParticipantRankingAdapter(responseBody?.data as ArrayList<DataParticipantRanking>, this@RankedActivity)
                        binding.rvParticipantRank.adapter = participantRankingAdapter
                        binding.rvParticipantRank.setHasFixedSize(true)
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ParticipantRankingResponse>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setGender() {
        binding.apply {
            rgGender.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton? = findViewById(checkedId)
                when (radio) {
                    rbMale -> {
                        gender = "laki-laki"
                    }
                    rbFemale -> {
                        gender = "perempuan"
                    }
                }
            }
        }
        Log.d("gender", gender)
    }

    private fun setLoading(condition: Boolean) {
        if (condition) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "Ranked"
    }
}