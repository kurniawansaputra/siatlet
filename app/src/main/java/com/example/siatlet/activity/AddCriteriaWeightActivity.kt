package com.example.siatlet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityAddCriteriaWeightBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.CriteriaResponse
import com.example.siatlet.model.DataCriteria
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCriteriaWeightActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var nameContest: String
    private lateinit var weight: String

    private lateinit var idCriteria: String
    private lateinit var nameCriteria: String

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityAddCriteriaWeightBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCriteriaWeightBinding.inflate(layoutInflater)
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
            toolbar.subtitle = nameContest
        }
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idContest = intent.getStringExtra("id_contest").toString()
        nameContest = intent.getStringExtra("name_contest").toString()
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
                        val contestAdapter = ArrayAdapter(this@AddCriteriaWeightActivity, R.layout.layout_dropdown, nameList)
                        binding.layoutAddCriteriaWeight.spCriteria.adapter = contestAdapter

                        binding.layoutAddCriteriaWeight.spCriteria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                                idCriteria = criteria[position].idKriteria.toString()
                                nameCriteria = criteria[position].namaKriteria.toString()
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setListener() {
        binding.apply {
            layoutAddCriteriaWeight.buttonAddUpdate.text = getString(R.string.add)
            layoutAddCriteriaWeight.buttonAddUpdate.setOnClickListener {
                weight = binding.layoutAddCriteriaWeight.editWeight.text.toString()
                if (weight.isEmpty()) {
                    alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
                } else {
                    addCriteriaWeight()
                }
            }
        }
    }

    private fun addCriteriaWeight() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().addCriteriaWeight(token, idContest, idCriteria, weight)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code
                val message = response.body()?.meta?.message

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToCriteriaWeight()
                        toast("Bobot kriteria berhasil ditambahkan.")
                    } else {
                        alert(R.drawable.ic_warning, "Peringatan", "$message", R.color.red)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MetaResponse>, t: Throwable) {
                dialog.hideDialog()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun goToCriteriaWeight() {
        val intent = Intent(this, CriteriaWeightActivity::class.java)
        intent.putExtra("id_contest", idContest)
        intent.putExtra("name_contest", nameContest)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "AddCriteriaWeight"
    }
}