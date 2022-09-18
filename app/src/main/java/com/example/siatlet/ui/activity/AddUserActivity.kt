package com.example.siatlet.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityAddUserBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUserActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var name: String
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var phone: String
    private lateinit var address: String

    private val level = arrayOf("Admin", "Pelatih", "Peserta")
    private val gender = arrayOf("Laki-laki", "Perempuan")

    private var nameLevel: String = ""
    private var idLevel: String = ""

    private var nameGender: String = ""
    private var idGender: String = ""

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityAddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setSpLevel()
        setSpGender()
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


    private fun setSpLevel() {
        val levelAdapter = ArrayAdapter(this, R.layout.layout_dropdown, level)
        binding.spLevel.adapter = levelAdapter

        binding.spLevel.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                nameLevel = level[p2]

                when (nameLevel) {
                    "Admin" -> {
                        idLevel = "1"
                    }
                    "Pelatih" -> {
                        idLevel = "2"
                    }
                    "Peserta" -> {
                        idLevel = "3"
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setSpGender() {
        val genderAdapter = ArrayAdapter(this, R.layout.layout_dropdown, gender)
        binding.spGender.adapter = genderAdapter

        binding.spGender.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                nameGender = gender[p2]

                when (nameGender) {
                    "Laki-laki" -> {
                        idGender = "1"
                    }
                    "Perempuan" -> {
                        idGender = "2"
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setListener() {
        binding.buttonAdd.setOnClickListener {
            name = binding.editName.text.toString()
            username = binding.editUsername.text.toString().trim()
            password = binding.editPassword.text.toString().trim()
            phone = binding.editPhone.text.toString().trim()
            address = binding.editAddress.toString()

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
            } else {
                dialog.showProgressDialog(this)
                val client = ApiConfig.getApiService().addUser(token, username, password, idLevel, name, phone, address, idGender)
                client.enqueue(object : Callback<MetaResponse> {
                    override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                        dialog.hideDialog()
                        val statusCode = response.body()?.meta?.code
                        val message = response.body()?.meta?.message

                        if (response.isSuccessful) {
                            if (statusCode == "200") {
                                goToUser()
                                toast("User berhasil ditambahkan.")
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
        }
    }

    private fun goToUser() {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
        finish()
    }

    companion object {
        const val TAG = "AddUser"
    }
}