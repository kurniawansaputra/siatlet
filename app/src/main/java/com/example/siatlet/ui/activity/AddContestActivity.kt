package com.example.siatlet.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityAddContestBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddContestActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var name: String
    private lateinit var date: String

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityAddContestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddContestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setListener()
    }

    private fun init() {
        dialog = DialogUtil()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()
    }


    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun setListener() {
        binding.apply {
            layoutAddContest.editDate.setOnClickListener {
                setDate()
            }

            layoutAddContest.buttonAddUpdate.text = getString(R.string.add)
            layoutAddContest.buttonAddUpdate.setOnClickListener {
                name = binding.layoutAddContest.editName.text.toString()
                date = binding.layoutAddContest.editDate.text.toString()

                if (name.isEmpty() || date.isEmpty()) {
                    alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
                } else {
                    addContest()
                }
            }
        }
    }


    private fun addContest() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().addContest(token, name, date)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code
                val message = response.body()?.meta?.message

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToContest()
                        toast("Lomba berhasil ditambahkan.")
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

    private fun setDate() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _: DatePicker, i: Int, i1: Int, i2: Int ->
            binding.layoutAddContest.editDate.setText("$i-${i1 + 1}-$i2")
        }, year, month, day).show()
    }

    private fun goToContest() {
        val intent = Intent(this, ContestActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "AddContest"
    }
}