package com.example.siatlet.ui.activity

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
import com.example.siatlet.databinding.ActivityDetailUserBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.model.UserByIdResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : BaseActivity() {
    private lateinit var idUser: String
    private lateinit var token: String
    private lateinit var name: String
    private lateinit var username: String
    private lateinit var phone: String
    private lateinit var address: String

    private val level = arrayOf("Admin", "Pelatih", "Pemilik")
    private val gender = arrayOf("Laki-laki", "Perempuan")

    private var nameLevel: String = ""
    private var idLevel: String = ""

    private var nameGender: String = ""
    private var idGender: String = ""

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setToolbar()
        setListener()
        setDetail()
        setSpLevel()
        setSpGender()
    }

    private fun init() {
        dialog = DialogUtil()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idUser = intent.getStringExtra("id_user").toString()
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
                        deleteUserById()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun setListener() {
        binding.apply {
            layoutUpdateUser.labelPassword.visibility = View.GONE
            layoutUpdateUser.editPassword.visibility = View.GONE
            layoutUpdateUser.buttonAddUpdate.text = getString(R.string.update)
            layoutUpdateUser.buttonAddUpdate.setOnClickListener {
                name = binding.layoutUpdateUser.editName.text.toString()
                username = binding.layoutUpdateUser.editUsername.text.toString().trim()
                phone = binding.layoutUpdateUser.editPhone.text.toString().trim()
                address = binding.layoutUpdateUser.editAddress.text.toString()

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
                        updateUser()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun setDetail() {
        setLoading(true)
        val client = ApiConfig.getApiService().getUserById(token, idUser)
        client.enqueue(object : Callback<UserByIdResponse> {
            override fun onResponse(call: Call<UserByIdResponse>, response: Response<UserByIdResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                val responseBody = response.body()?.data

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        idLevel = responseBody?.level.toString()
                        name = responseBody?.nama.toString()
                        username = responseBody?.username.toString()
                        idGender = responseBody?.jenisKelamin.toString()
                        phone = responseBody?.noHp.toString()
                        address = responseBody?.alamat.toString()

                        binding.apply {
                            layoutUpdateUser.editName.setText(name)
                            layoutUpdateUser.editUsername.setText(username)
                            layoutUpdateUser.editPhone.setText(phone)
                            layoutUpdateUser.editAddress.setText(address)
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserByIdResponse>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setSpLevel() {
        val levelAdapter = ArrayAdapter(this, R.layout.layout_dropdown, level)
        binding.layoutUpdateUser.spLevel.adapter = levelAdapter

        binding.layoutUpdateUser.spLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                nameLevel = level[p2]

                when (nameLevel) {
                    "Admin" -> {
                        idLevel = "1"
                    }
                    "Pelatih" -> {
                        idLevel = "2"
                    }
                    "Pemilik" -> {
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
        binding.layoutUpdateUser.spGender.adapter = genderAdapter

        binding.layoutUpdateUser.spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun updateUser() {
        if (name.isEmpty() || username.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
        } else {
            dialog.showProgressDialog(this)
            val client = ApiConfig.getApiService().updateUser(token, idUser, username, idLevel, name, phone, address, idGender)
            client.enqueue(object : Callback<MetaResponse> {
                override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                    dialog.hideDialog()
                    val statusCode = response.body()?.meta?.code

                    if (response.isSuccessful) {
                        if (statusCode == "200") {
                            goToUser()
                            toast("User berhasil diupdate.")
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

    private fun deleteUserById() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().deleteUser(token, idUser)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToUser()
                        toast("User berhasil dihapus.")
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

    private fun setLoading(condition: Boolean) {
        if (condition) {
            binding.progressCircular.visibility = View.VISIBLE
            binding.containerLayout.visibility = View.GONE
        } else {
            binding.progressCircular.visibility = View.GONE
            binding.containerLayout.visibility = View.VISIBLE
        }
    }

    private fun goToUser() {
        val intent = Intent(this, UserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
    companion object {
        const val TAG = "DetailUser"
    }
}