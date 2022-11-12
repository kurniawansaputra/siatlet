package com.example.siatlet.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityDetailCriteriaValueBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.CriteriaValueByIdResponse
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCriteriaValueActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idCriteriaValue: String
    private lateinit var idCriteria: String
    private lateinit var nameCriteria: String
    private lateinit var nameContest: String
    private var gender = ""
    private lateinit var value: String
    private lateinit var desc: String

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityDetailCriteriaValueBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCriteriaValueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setListener()
        setDetail()
        setGender()
    }

    private fun init() {
        dialog = DialogUtil()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idCriteriaValue = intent.getStringExtra("id_criteria_value").toString()
        idCriteria = intent.getStringExtra("id_criteria").toString()
        nameCriteria = intent.getStringExtra("name_criteria").toString()
        nameContest = intent.getStringExtra("name_contest").toString()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            toolbar.title = nameCriteria
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
                        deleteCriteriaValueById()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun deleteCriteriaValueById() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().deleteCriteriaValue(token, idCriteriaValue)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToCriteriaValue()
                        toast("Nilai kriteria berhasil dihapus.")
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
        val client = ApiConfig.getApiService().getCriteriaValueById(token, idCriteriaValue)
        client.enqueue(object : Callback<CriteriaValueByIdResponse> {
            override fun onResponse(call: Call<CriteriaValueByIdResponse>, response: Response<CriteriaValueByIdResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                val responseBody = response.body()?.data

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        gender = responseBody?.jenisKelamin.toString()
                        value = responseBody?.nilai.toString()
                        desc = responseBody?.keterangan.toString()

                        binding.apply {
                            layoutUpdateCriteriaValue.editValue.setText(value)
                            layoutUpdateCriteriaValue.editDesc.setText(desc)

                            if (gender == "laki-laki") {
                                layoutUpdateCriteriaValue.rbMale.isChecked = true
                            } else {
                                layoutUpdateCriteriaValue.rbFemale.isChecked = true
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CriteriaValueByIdResponse>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setListener() {
        binding.apply {
            layoutUpdateCriteriaValue.buttonAddUpdate.text = getString(R.string.update)
            layoutUpdateCriteriaValue.buttonAddUpdate.setOnClickListener {
                value = binding.layoutUpdateCriteriaValue.editValue.text.toString()
                desc = binding.layoutUpdateCriteriaValue.editDesc.text.toString()

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
                        updateCriteriaValue()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun updateCriteriaValue() {
        if (value.isEmpty() || desc.isEmpty()) {
            alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
        } else {
            dialog.showProgressDialog(this)
            val client = ApiConfig.getApiService().updateCriteriaValue(token, idCriteriaValue, idCriteria, gender, value, desc)
            client.enqueue(object : Callback<MetaResponse> {
                override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                    dialog.hideDialog()
                    val statusCode = response.body()?.meta?.code

                    if (response.isSuccessful) {
                        if (statusCode == "200") {
                            goToCriteriaValue()
                            toast("Nilai kriteria berhasil diupdate.")
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

    private fun setGender() {
        binding.apply {
            layoutUpdateCriteriaValue.rgGender.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton? = findViewById(checkedId)
                when (radio) {
                    layoutUpdateCriteriaValue.rbMale -> {
                        gender = "laki-laki"
                    }
                    layoutUpdateCriteriaValue.rbFemale -> {
                        gender = "perempuan"
                    }
                }
            }
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

    companion object {
        const val TAG = "DetailCriteriaValue"
    }
}