package com.example.siatlet.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityAddParticipantBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AddParticipantActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var nameContest: String

    private lateinit var name: String
    private lateinit var placeOfBirth: String
    private lateinit var dateOfBirth: String
    private var gender: String = "laki-laki"
    private lateinit var weight: String
    private lateinit var occupation: String
    private lateinit var address: String

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityAddParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setGender()
        setListener()
    }

    private fun init() {
        dialog = DialogUtil()

        binding.layoutAddParticipant.rbMale.isChecked = true

        binding.layoutAddParticipant.labelNumberRegistration.visibility = View.GONE
        binding.layoutAddParticipant.editNumberRegistration.visibility = View.GONE
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

    private fun setListener() {
        binding.apply {
            layoutAddParticipant.editDate.setOnClickListener {
                setDate()
            }

            layoutAddParticipant.buttonAddUpdate.text = getString(R.string.add)
            layoutAddParticipant.buttonAddUpdate.setOnClickListener {
                name = binding.layoutAddParticipant.editName.text.toString()
                placeOfBirth = binding.layoutAddParticipant.editPlaceOfBirth.text.toString()
                dateOfBirth = binding.layoutAddParticipant.editDate.text.toString()
                weight = binding.layoutAddParticipant.editWeight.text.toString()
                occupation = binding.layoutAddParticipant.editWeight.text.toString()
                address = binding.layoutAddParticipant.editAddress.text.toString()

                if (name.isEmpty() || placeOfBirth.isEmpty() || dateOfBirth.isEmpty() || weight.isEmpty() || occupation.isEmpty() || address.isEmpty()) {
                    alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
                } else {
                    addParticipant()
                }
            }
        }
    }

    private fun addParticipant() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().addParticipant(token, idContest, name, placeOfBirth, dateOfBirth, gender, weight, address, occupation)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code
                val message = response.body()?.meta?.message

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToParticipant()
                        toast("Peserta berhasil ditambahkan.")
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
            binding.layoutAddParticipant.editDate.setText("$i-${i1 + 1}-$i2")
        }, year, month, day).show()
    }

    private fun setGender() {
        binding.apply {
            layoutAddParticipant.rgGender.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton? = findViewById(checkedId)
                when (radio) {
                    layoutAddParticipant.rbMale -> {
                        gender = "laki-laki"
                    }
                    layoutAddParticipant.rbFemale -> {
                        gender = "perempuan"
                    }
                }
            }
        }
    }

    private fun goToParticipant() {
        val intent = Intent(this, ParticipantActivity::class.java)
        intent.putExtra("id_contest", idContest)
        intent.putExtra("name_contest", nameContest)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "AddParticipant"
    }
}