package com.example.siatlet.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityDetailCriteriaBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.*
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCriteriaActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idCriteria: String

    private lateinit var name: String
    private var property = ""
    private var contest: String = ""
    private var idContest: String = ""
    private var nameContest: String = ""

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityDetailCriteriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCriteriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setDetail()
        setListener()
        setProperty()
    }

    private fun init() {
        dialog = DialogUtil()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idCriteria = intent.getStringExtra("id_criteria").toString()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }

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
                        deleteCriteriaById()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun deleteCriteriaById() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().deleteCriteria(token, idCriteria)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToCriteria()
                        toast("Kriteria berhasil dihapus.")
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

    private fun setDetail() {
        setLoading(true)
        val client = ApiConfig.getApiService().getCriteriaById(token, idCriteria)
        client.enqueue(object : Callback<CriteriaByIdResponse> {
            override fun onResponse(call: Call<CriteriaByIdResponse>, response: Response<CriteriaByIdResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                val responseBody = response.body()?.data

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        name = responseBody?.namaKriteria.toString()
                        property = responseBody?.sifat.toString()
                        contest = responseBody?.namaLomba.toString()

                        setSpContest()

                        binding.apply {
                            layoutUpdateCriteria.editName.setText(name)

                            if (property == "Benefit") {
                                layoutUpdateCriteria.rbBenefit.isChecked = true
                            } else {
                                layoutUpdateCriteria.rbCost.isChecked = true
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaByIdResponse>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setSpContest() {
        val client = ApiConfig.getApiService().getAllContest(token)
        client.enqueue(object : Callback<ContestResponse> {
            override fun onResponse(call: Call<ContestResponse>, response: Response<ContestResponse>) {
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val contestData: List<DataContest> = response.body()!!.data as List<DataContest>
                        val nameList: MutableList<String> = ArrayList()

                        for (i in contestData.indices) {
                            nameList.add(
                                contestData[i].namaLomba.toString()
                            )
                        }
                        val contestAdapter = ArrayAdapter(this@DetailCriteriaActivity, R.layout.layout_dropdown, nameList)
                        binding.layoutUpdateCriteria.spContest.adapter = contestAdapter
                        val spinnerPosition: Int = contestAdapter.getPosition(contest)
                        binding.layoutUpdateCriteria.spContest.setSelection(spinnerPosition)

                        binding.layoutUpdateCriteria.spContest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                                idContest = contestData[position].idLomba.toString()
                                nameContest = contestData[position].namaLomba.toString()
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ContestResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setProperty() {
        binding.apply {
            layoutUpdateCriteria.rgProperty.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton? = findViewById(checkedId)
                when (radio) {
                    layoutUpdateCriteria.rbBenefit -> {
                        property = "Benefit"
                    }
                    layoutUpdateCriteria.rbCost -> {
                        property = "Cost"
                    }
                }
            }
        }
    }

    private fun setListener() {
        binding.apply {
            layoutUpdateCriteria.buttonAddUpdate.text = getString(R.string.update)
            layoutUpdateCriteria.buttonAddUpdate.setOnClickListener {
                name = binding.layoutUpdateCriteria.editName.text.toString()

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
                        updateCriteria()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun updateCriteria() {
        if (name.isEmpty()) {
            alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
        } else {
            dialog.showProgressDialog(this)
            val client = ApiConfig.getApiService().updateCriteria(token, name, property, idContest, idCriteria)
            client.enqueue(object : Callback<MetaResponse> {
                override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                    dialog.hideDialog()
                    val statusCode = response.body()?.meta?.code

                    if (response.isSuccessful) {
                        if (statusCode == "200") {
                            goToCriteria()
                            toast("Kriteria berhasil diupdate.")
                        }
                    } else {
                        Log.e(DetailContestActivity.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<MetaResponse>, t: Throwable) {
                    dialog.hideDialog()
                    Log.e(DetailContestActivity.TAG, "onFailure: ${t.message}")
                }
            })
        }
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


    private fun goToCriteria() {
        val intent = Intent(this, CriteriaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "DetailCriteria"
    }
}