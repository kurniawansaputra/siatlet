package com.example.siatlet.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityDetailParticipantBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.model.ParticipantByIdResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailParticipantActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idParticipant: String
    private lateinit var idContest: String
    private lateinit var nameContest: String

    private lateinit var name: String
    private lateinit var placeOfBirth: String
    private lateinit var dateOfBirth: String
    private var gender: String = ""
    private lateinit var numberRegistration: String
    private lateinit var weight: String
    private lateinit var occupation: String
    private lateinit var address: String

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityDetailParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailParticipantBinding.inflate(layoutInflater)
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

        idParticipant = intent.getStringExtra("id_participant").toString()
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
                        deleteParticipant()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun deleteParticipant() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().deleteParticipant(token, idParticipant)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToParticipant()
                        toast("Peserta berhasil dihapus.")
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
        val client = ApiConfig.getApiService().getParticipantById(token, idParticipant)
        client.enqueue(object : Callback<ParticipantByIdResponse> {
            override fun onResponse(call: Call<ParticipantByIdResponse>, response: Response<ParticipantByIdResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                val responseBody = response.body()?.data

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        numberRegistration = responseBody?.noReg.toString()
                        name = responseBody?.namaPeserta.toString()
                        placeOfBirth = responseBody?.tempatLahir.toString()
                        dateOfBirth = responseBody?.tanggalLahir.toString()
                        gender = responseBody?.jenisKelamin.toString()
                        occupation = responseBody?.pekerjaan.toString()
                        address = responseBody?.alamat.toString()
                        weight = responseBody?.beratBadan.toString()

                        binding.apply {
                            layoutUpdateParticipant.editNumberRegistration.setText(numberRegistration)
                            layoutUpdateParticipant.editName.setText(name)
                            layoutUpdateParticipant.editPlaceOfBirth.setText(placeOfBirth)
                            layoutUpdateParticipant.editDate.setText(dateOfBirth)
                            layoutUpdateParticipant.editJob.setText(occupation)
                            layoutUpdateParticipant.editAddress.setText(address)
                            layoutUpdateParticipant.editWeight.setText(weight)

                            if (gender == "laki-laki") {
                                layoutUpdateParticipant.rbMale.isChecked = true
                            } else {
                                layoutUpdateParticipant.rbFemale.isChecked = true
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ParticipantByIdResponse>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setGender() {
        binding.apply {
            layoutUpdateParticipant.rgGender.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton? = findViewById(checkedId)
                when (radio) {
                    layoutUpdateParticipant.rbMale -> {
                        gender = "laki-laki"
                    }
                    layoutUpdateParticipant.rbFemale -> {
                        gender = "perempuan"
                    }
                }
            }
        }
    }

    private fun setListener() {
        binding.layoutUpdateParticipant.editDate.setOnClickListener {
            setDate()
        }

        binding.apply {
            layoutUpdateParticipant.buttonAddUpdate.text = getString(R.string.update)
            layoutUpdateParticipant.buttonAddUpdate.setOnClickListener {
                name = binding.layoutUpdateParticipant.editName.text.toString()
                placeOfBirth = binding.layoutUpdateParticipant.editPlaceOfBirth.text.toString()
                dateOfBirth = binding.layoutUpdateParticipant.editDate.text.toString()
                weight = binding.layoutUpdateParticipant.editWeight.text.toString()
                occupation = binding.layoutUpdateParticipant.editJob.text.toString()
                address = binding.layoutUpdateParticipant.editAddress.text.toString()

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
                        updateParticipant()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun updateParticipant() {
        if (name.isEmpty() || placeOfBirth.isEmpty() || dateOfBirth.isEmpty() || weight.isEmpty() || occupation.isEmpty() || address.isEmpty()) {
            alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
        } else {
            dialog.showProgressDialog(this)
            val client = ApiConfig.getApiService().updateParticipant(token, idParticipant, idContest, numberRegistration, name, placeOfBirth, dateOfBirth, gender, weight, address, occupation)
            client.enqueue(object : Callback<MetaResponse> {
                override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                    dialog.hideDialog()
                    val statusCode = response.body()?.meta?.code

                    if (response.isSuccessful) {
                        if (statusCode == "200") {
                            goToParticipant()
                            toast("Peserta berhasil diupdate.")
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

    private fun setDate() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _: DatePicker, i: Int, i1: Int, i2: Int ->
            binding.layoutUpdateParticipant.editDate.setText("$i-${i1 + 1}-$i2")
        }, year, month, day).show()
    }

    private fun goToParticipant() {
        val intent = Intent(this, ParticipantActivity::class.java)
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
        const val TAG = "DetailParticipant"
    }

}