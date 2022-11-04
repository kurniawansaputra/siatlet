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
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityDetailCriteriaWeightBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.*
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCriteriaWeightActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var nameContest: String
    private lateinit var idCriteriaWeight: String

    private var idCriteria = ""
    private var nameCriteria = ""
    private var weight = ""

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityDetailCriteriaWeightBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCriteriaWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setListener()
        setDetail()
    }

    private fun init() {
        dialog = DialogUtil()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idCriteriaWeight = intent.getStringExtra("id_criteria_weight").toString()
        idContest = intent.getStringExtra("id_contest").toString()
        nameContest = intent.getStringExtra("name_contest").toString()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
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
                        deleteCriteriaWeightById()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun deleteCriteriaWeightById() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().deleteCriteriaWeight(token, idCriteriaWeight)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToCriteriaWeight()
                        toast("Bobot kriteria berhasil dihapus.")
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

    private fun setListener() {
        binding.apply {
            layoutUpdateCriteriaWeight.buttonAddUpdate.text = getString(R.string.update)
            layoutUpdateCriteriaWeight.buttonAddUpdate.setOnClickListener {
                weight = binding.layoutUpdateCriteriaWeight.editWeight.text.toString()

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
                        updateCriteriaWeight()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun updateCriteriaWeight() {
        if (weight.isEmpty()) {
            alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
        } else {
            dialog.showProgressDialog(this)
            val client = ApiConfig.getApiService().updateCriteriaWeight(token, idCriteriaWeight, idContest, idCriteria, weight)
            client.enqueue(object : Callback<MetaResponse> {
                override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                    dialog.hideDialog()
                    val statusCode = response.body()?.meta?.code

                    if (response.isSuccessful) {
                        if (statusCode == "200") {
                            goToCriteriaWeight()
                            toast("Bobot kriteria berhasil diupdate.")
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
    }

    private fun setDetail() {
        setLoading(true)
        val client = ApiConfig.getApiService().getCriteriaWeightById(token, idCriteriaWeight)
        client.enqueue(object : Callback<CriteriaWeightByIdResponse> {
            override fun onResponse(call: Call<CriteriaWeightByIdResponse>, response: Response<CriteriaWeightByIdResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                val responseBody = response.body()?.data

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        weight = responseBody?.bobot.toString()
                        nameCriteria = responseBody?.namaKriteria.toString()

                        setSpCriteria()

                        binding.apply {
                            layoutUpdateCriteriaWeight.editWeight.setText(weight)
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaWeightByIdResponse>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setSpCriteria() {
        val client = ApiConfig.getApiService().getCriteriatByIdContest(token, idContest)
        client.enqueue(object : Callback<CriteriaResponse> {
            override fun onResponse(call: Call<CriteriaResponse>, response: Response<CriteriaResponse>) {
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val criteriaData: List<DataCriteria> = response.body()!!.data as List<DataCriteria>
                        val nameList: MutableList<String> = ArrayList()

                        for (i in criteriaData.indices) {
                            nameList.add(
                                criteriaData[i].namaKriteria.toString()
                            )
                        }
                        val contestAdapter = ArrayAdapter(this@DetailCriteriaWeightActivity, R.layout.layout_dropdown, nameList)
                        binding.layoutUpdateCriteriaWeight.spCriteria.adapter = contestAdapter
                        val spinnerPosition: Int = contestAdapter.getPosition(nameCriteria)
                        binding.layoutUpdateCriteriaWeight.spCriteria.setSelection(spinnerPosition)

                        binding.layoutUpdateCriteriaWeight.spCriteria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                                idCriteria = criteriaData[position].idKriteria.toString()
                                nameCriteria = criteriaData[position].namaKriteria.toString()
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

    private fun goToCriteriaWeight() {
        val intent = Intent(this, CriteriaWeightActivity::class.java)
        intent.putExtra("id_contest", idContest)
        intent.putExtra("name_contest", nameContest)
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

    companion object {
        const val TAG = "DetailCriteriaWeight"
    }
}