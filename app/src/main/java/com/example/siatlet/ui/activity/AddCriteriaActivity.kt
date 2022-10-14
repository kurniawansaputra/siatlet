package com.example.siatlet.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityAddCriteriaBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.ContestResponse
import com.example.siatlet.model.DataContest
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCriteriaActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var name: String

    private var idContest: String = ""
    private var nameContest: String = ""
    private var property: String = "Benefit"

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityAddCriteriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCriteriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setSpContest()
        setProperty()
        setToolbar()
        setListener()
    }

    private fun init() {
        dialog = DialogUtil()

        binding.layoutAddCriteria.rbBenefit.isChecked = true
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()
    }

    private fun setSpContest() {
        val client = ApiConfig.getApiService().getAllContest(token)
        client.enqueue(object : Callback<ContestResponse> {
            override fun onResponse(call: Call<ContestResponse>, response: Response<ContestResponse>) {
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val contest: List<DataContest> = response.body()!!.data as List<DataContest>
                        val nameList: MutableList<String> = ArrayList()

                        for (i in contest.indices) {
                            nameList.add(
                                contest[i].namaLomba.toString()
                            )
                        }
                        val contestAdapter = ArrayAdapter(this@AddCriteriaActivity, R.layout.layout_dropdown, nameList)
                        binding.layoutAddCriteria.spContest.adapter = contestAdapter

                        binding.layoutAddCriteria.spContest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                                idContest = contest[position].idLomba.toString()
                                nameContest = contest[position].namaLomba.toString()
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
            layoutAddCriteria.rgProperty.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton? = findViewById(checkedId)
                when (radio) {
                    layoutAddCriteria.rbBenefit -> {
                        property = "Benefit"
                    }
                    layoutAddCriteria.rbCost -> {
                        property = "Cost"
                    }
                }
            }
        }
    }

    private fun setListener() {
        binding.apply {
            layoutAddCriteria.buttonAddUpdate.text = getString(R.string.add)
            layoutAddCriteria.buttonAddUpdate.setOnClickListener {
                name = binding.layoutAddCriteria.editName.text.toString()
                if (name.isEmpty()) {
                    alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
                } else {
                    addCriteria()
                }
            }
        }
    }

    private fun addCriteria() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().addCriteria(token, name, property, idContest)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code
                val message = response.body()?.meta?.message

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToCriteria()
                        toast("Kriteria berhasil ditambahkan.")
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

    private fun goToCriteria() {
        val intent = Intent(this, CriteriaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "AddCriteria"
    }
}