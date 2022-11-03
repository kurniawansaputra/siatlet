package com.example.siatlet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityAddUserBinding
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUserActivity : BaseActivity() {
    private lateinit var name: String
    private lateinit var username: String

    private val levelList = arrayOf("admin", "pelatih", "pemilik")
    private var level: String = ""

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityAddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setToolbar()
        setSpLevel()
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
        }
    }

    private fun setSpLevel() {
        val levelAdapter = ArrayAdapter(this, R.layout.layout_dropdown, levelList)
        binding.layoutAddUser.spLevel.adapter = levelAdapter

        binding.layoutAddUser.spLevel.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                level = levelList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

//    private fun setSpGender() {
//        val genderAdapter = ArrayAdapter(this, R.layout.layout_dropdown, gender)
//        binding.layoutAddUser.spGender.adapter = genderAdapter
//
//        binding.layoutAddUser.spGender.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                nameGender = gender[p2]
//
//                when (nameGender) {
//                    "Laki-laki" -> {
//                        idGender = "1"
//                    }
//                    "Perempuan" -> {
//                        idGender = "2"
//                    }
//                }
//
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }
//    }

    private fun setListener() {
        binding.apply {
            layoutAddUser.buttonAddUpdate.text = getString(R.string.add)
            layoutAddUser.buttonAddUpdate.setOnClickListener {
                name = binding.layoutAddUser.editName.text.toString()
                username = binding.layoutAddUser.editUsername.text.toString().trim()

                if (name.isEmpty() || username.isEmpty()) {
                    alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
                } else {
                    addUser()
                }
            }
        }
    }

    private fun addUser() {
        dialog.showProgressDialog(this@AddUserActivity)
        val client = ApiConfig.getApiService().addUser(username, level, name)
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

    private fun goToUser() {
        val intent = Intent(this, UserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "AddUser"
    }
}