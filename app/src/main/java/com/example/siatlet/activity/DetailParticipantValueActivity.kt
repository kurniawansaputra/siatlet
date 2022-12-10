package com.example.siatlet.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityDetailParticipantValueBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.*
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class DetailParticipantValueActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idParticipantValue: String
    private lateinit var idContest: String
    private lateinit var nameContest: String
    private lateinit var nameParticipant: String
    private lateinit var idParticipant: String

    private var idCriteria: String = ""
    private var nameCriteria = ""
    private var idCriteriaValue = ""
    private var nameCriteriaValue = ""
    private var nameCriteriaValue2 = ""

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityDetailParticipantValueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailParticipantValueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setDetail()
        setListener()
    }


    private fun init() {
        dialog = DialogUtil()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idParticipantValue = intent.getStringExtra("id_participant_value").toString()
        idParticipant = intent.getStringExtra("id_participant").toString()
        idContest = intent.getStringExtra("id_contest").toString()
        nameContest = intent.getStringExtra("name_contest").toString()
        nameParticipant = intent.getStringExtra("name_participant").toString()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            toolbar.title = "Detail Nilai Peserta - $nameParticipant"
            toolbar.subtitle = nameContest

            ivDelete.setOnClickListener {
                val binding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
                val builder: AlertDialog.Builder = AlertDialog.Builder(layoutInflater.context)
                builder.setView(binding.root)
                val dialog: AlertDialog = builder.create()
                binding.apply {
                    labelTitle.text = getString(R.string.title_delete)
                    labelMessage.text = getString(R.string.message_delete)
                    buttonNo.setOnClickListener {
                        dialog.dismiss()
                    }
                    buttonYes.setOnClickListener {
                        deleteParticipantValue()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun deleteParticipantValue() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().deleteParticipantValue(token, idParticipantValue)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToParticipantValue()
                        toast("Nilai Peserta berhasil dihapus.")
                    }
                } else {
                    Log.e(DetailCriteriaWeightActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MetaResponse>, t: Throwable) {
                dialog.hideDialog()
                Log.e(DetailCriteriaWeightActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setDetail() {
        setLoading(true)
        val client = ApiConfig.getApiService().getParticipantValueById(token, idParticipantValue)
        client.enqueue(object : Callback<ParticipantValueByIdResponse> {
            override fun onResponse(call: Call<ParticipantValueByIdResponse>, response: Response<ParticipantValueByIdResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                val responseBody = response.body()?.data

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        nameCriteria = responseBody?.namaKriteria.toString()
                        nameCriteriaValue = responseBody?.ketNilaiKriteria.toString()

                        setSpCriteria()
                        Log.d("criteria", nameCriteriaValue)
                    }
                } else {
                    Log.e(DetailCriteriaWeightActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ParticipantValueByIdResponse>, t: Throwable) {
                setLoading(false)
                Log.e(DetailCriteriaWeightActivity.TAG, "onFailure: ${t.message}")
            }
        })
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
                        val contestAdapter = ArrayAdapter(this@DetailParticipantValueActivity, R.layout.layout_dropdown, nameList)
                        binding.layoutUpdateParticipantValue.spCriteria.adapter = contestAdapter
                        val spinnerPosition: Int = contestAdapter.getPosition(nameCriteria)
                        binding.layoutUpdateParticipantValue.spCriteria.setSelection(spinnerPosition)

                        binding.layoutUpdateParticipantValue.spCriteria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                                idCriteria = criteria[position].idKriteria.toString()
                                nameCriteria = criteria[position].namaKriteria.toString()

                                setSpCriteriaValue()
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }
                    }
                } else {
                    Log.e(DetailCriteriaActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaResponse>, t: Throwable) {
                Log.e(DetailCriteriaActivity.TAG, "onFailure: ${t.message}")
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
                        val contestAdapter = ArrayAdapter(this@DetailParticipantValueActivity, R.layout.layout_dropdown, nameList)
                        binding.layoutUpdateParticipantValue.spCriteriaValue.adapter = contestAdapter
                        val spinnerPosition: Int = contestAdapter.getPosition(nameCriteriaValue)
                        binding.layoutUpdateParticipantValue.spCriteriaValue.setSelection(spinnerPosition)

                        binding.layoutUpdateParticipantValue.spCriteriaValue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            layoutUpdateParticipantValue.buttonAddUpdate.text = getString(R.string.update)
            layoutUpdateParticipantValue.buttonAddUpdate.setOnClickListener {
                val binding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
                val builder: AlertDialog.Builder = AlertDialog.Builder(layoutInflater.context)
                builder.setView(binding.root)
                val dialog: AlertDialog = builder.create()
                binding.apply {
                    labelTitle.text = getString(R.string.title_update)
                    labelMessage.text = getString(R.string.message_update)
                    buttonNo.setOnClickListener {
                        dialog.dismiss()
                    }
                    buttonYes.setOnClickListener {
                        updateParticipantValue()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun updateParticipantValue() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().updateParticipantValue(token, idParticipantValue, idParticipant, idContest, idCriteria, idCriteriaValue)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToParticipantValue()
                        toast("Peserta berhasil diupdate.")
                    }
                } else {
                    Log.e(DetailParticipantActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MetaResponse>, t: Throwable) {
                dialog.hideDialog()
                Log.e(DetailParticipantActivity.TAG, "onFailure: ${t.message}")
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

    private fun setLoading(condition: Boolean) {
        if (condition) {
            binding.progressCircular.visibility = View.VISIBLE
            binding.containerLayout.visibility = View.GONE
        } else {
            binding.progressCircular.visibility = View.GONE
            binding.containerLayout.visibility = View.VISIBLE
        }
    }
}