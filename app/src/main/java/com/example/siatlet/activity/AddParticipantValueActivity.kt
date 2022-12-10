package com.example.siatlet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityAddParticipantValueBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.*
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddParticipantValueActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var nameContest: String
    private lateinit var idParticipant: String
    private lateinit var nameParticipant: String

    private var idCriteria: String = ""
    private lateinit var nameCriteria: String
    private lateinit var idCriteriaValue: String
    private lateinit var nameCriteriaValue: String

    private lateinit var dialog: DialogUtil


    private lateinit var binding: ActivityAddParticipantValueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddParticipantValueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setSpCriteria()
        setListener()
    }

    private fun init() {
        dialog = DialogUtil()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            toolbar.title = "Tambah Nilai Peserta - $nameParticipant"
            toolbar.subtitle = nameContest
        }
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idContest = intent.getStringExtra("id_contest").toString()
        nameContest = intent.getStringExtra("name_contest").toString()
        idParticipant = intent.getStringExtra("id_participant").toString()
        nameParticipant = intent.getStringExtra("name_participant").toString()
    }

    private fun setSpCriteria() {
        val client = ApiConfig.getApiService().getCriteriaByIdContest(token, idContest)
        client.enqueue(object : Callback<CriteriaResponse> {
            override fun onResponse(call: Call<CriteriaResponse>, response: Response<CriteriaResponse>) {
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val criteria: List<DataCriteria> = response.body()!!.data as List<DataCriteria>
                        val nameList: MutableList<String> = ArrayList()

                        for (i in criteria.indices) {
                            nameList.add(
                                criteria[i].namaKriteria.toString()
                            )
                        }
                        val contestAdapter = ArrayAdapter(this@AddParticipantValueActivity, R.layout.layout_dropdown, nameList)
                        binding.layoutAddParticipantValue.spCriteria.adapter = contestAdapter

                        binding.layoutAddParticipantValue.spCriteria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                                idCriteria = criteria[position].idKriteria.toString()
                                nameCriteria = criteria[position].namaKriteria.toString()

                                setSpCriteriaValue()
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }
                    }
                } else {
                    Log.e(AddCriteriaWeightActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaResponse>, t: Throwable) {
                Log.e(AddCriteriaWeightActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setSpCriteriaValue() {
        val client = ApiConfig.getApiService().getCriteriaValueByIdCriteria(token, idCriteria)
        client.enqueue(object : Callback<CriteriaValueByIdCriteriaResponse> {
            override fun onResponse(call: Call<CriteriaValueByIdCriteriaResponse>, response: Response<CriteriaValueByIdCriteriaResponse>) {
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val criteriaValue: List<DataCriteriaValue> = response.body()!!.data as List<DataCriteriaValue>
                        val nameList: MutableList<String> = ArrayList()

                        for (i in criteriaValue.indices) {
                            nameList.add(
                                criteriaValue[i].nilai.toString() + ". " + criteriaValue[i].keterangan.toString() + " (" + criteriaValue[i].jenisKelamin.toString() + ")"
                            )
                        }
                        val contestAdapter = ArrayAdapter(this@AddParticipantValueActivity, R.layout.layout_dropdown, nameList)
                        binding.layoutAddParticipantValue.spCriteriaValue.adapter = contestAdapter

                        binding.layoutAddParticipantValue.spCriteriaValue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                                idCriteriaValue = criteriaValue[position].idNilaiKriteria.toString()
                                nameCriteriaValue = criteriaValue[position].keterangan.toString()
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }
                    }

                } else {
                    Log.e(CriteriaByContestActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaValueByIdCriteriaResponse>, t: Throwable) {
                Log.e(CriteriaByContestActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setListener() {
        binding.apply {
            layoutAddParticipantValue.buttonAddUpdate.text = getString(R.string.add)
            layoutAddParticipantValue.buttonAddUpdate.setOnClickListener {
                addParticipantValue()
            }
        }
    }

    private fun addParticipantValue() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().addParticipantValue(token, idParticipant, idContest, idCriteria, idCriteriaValue)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code
                val message = response.body()?.meta?.message

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToParticipantValue()
                        toast("Nilai Peserta berhasil ditambahkan.")
                    } else {
                        alert(R.drawable.ic_warning, "Peringatan", "$message", R.color.red)
                    }
                } else {
                    Log.e(AddParticipantActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MetaResponse>, t: Throwable) {
                dialog.hideDialog()
                Log.e(AddParticipantActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun goToParticipantValue() {
        val intent = Intent(this, ParticipantValueActivity::class.java)
        intent.putExtra("id_contest", idContest)
        intent.putExtra("name_contest", nameContest)
        intent.putExtra("id_participant", idParticipant)
        intent.putExtra("name_participant", nameParticipant)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}