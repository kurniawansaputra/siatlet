package com.example.siatlet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityAddCriteriaValueBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCriteriaValueActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idCriteria: String
    private lateinit var nameCriteria: String
    private lateinit var nameContest: String
    private var gender = "laki-laki"
    private lateinit var value: String
    private lateinit var desc: String

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityAddCriteriaValueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCriteriaValueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setGender()
        setListener()
    }

    private fun init() {
        dialog = DialogUtil()

        binding.layoutAddCriteriaValue.rbMale.isChecked = true
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            toolbar.title = nameCriteria
            toolbar.subtitle = nameContest
        }
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idCriteria = intent.getStringExtra("id_criteria").toString()
        nameCriteria = intent.getStringExtra("name_criteria").toString()
        nameContest = intent.getStringExtra("name_contest").toString()
    }

    private fun setGender() {
        binding.apply {
            layoutAddCriteriaValue.rgGender.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton? = findViewById(checkedId)
                when (radio) {
                    layoutAddCriteriaValue.rbMale -> {
                        gender = "laki-laki"
                    }
                    layoutAddCriteriaValue.rbFemale -> {
                        gender = "perempuan"
                    }
                }
            }
        }
    }

    private fun setListener() {
        binding.apply {
            layoutAddCriteriaValue.buttonAddUpdate.text = getString(R.string.add)
            layoutAddCriteriaValue.buttonAddUpdate.setOnClickListener {
                value = binding.layoutAddCriteriaValue.editValue.text.toString()
                desc = binding.layoutAddCriteriaValue.editDesc.text.toString()

                if (value.isEmpty() || desc.isEmpty()) {
                    alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
                } else {
                    addCriteriaValue()
                }
            }
        }
    }

    private fun addCriteriaValue() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().addCriteriaValue(token, idCriteria, gender, value, desc)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code
                val message = response.body()?.meta?.message

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToCriteriaValue()
                        toast("Nilai kriteria berhasil ditambahkan.")
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

    private fun goToCriteriaValue() {
        val intent = Intent(this, CriteriaValueActivity::class.java)
        intent.putExtra("id_criteria", idCriteria)
        intent.putExtra("name_criteria", nameCriteria)
        intent.putExtra("name_contest", nameContest)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "AddCriteriaWeight"
    }
}