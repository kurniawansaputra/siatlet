package com.example.siatlet.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityChangePasswordBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var level: String
    private lateinit var idUser: String
    private lateinit var newPassword: String
    private lateinit var confirmNewPassword: String

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setToolbar()
        setPref()
        setListener()
    }

    private fun init() {
        dialog = DialogUtil()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        idUser = user.data?.idUser.toString()
        level = user.data?.level.toString()
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
            buttonChangePassword.setOnClickListener {
                val binding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
                val builder: AlertDialog.Builder = AlertDialog.Builder(layoutInflater.context)
                builder.setView(binding.root)
                val dialog: AlertDialog = builder.create()
                binding.apply {
                    labelTitle.text = "Ubah Password"
                    labelMessage.text = "Apakah anda yakin ingin merubah password anda?"
                    buttonNo.setOnClickListener {
                        dialog.dismiss()
                    }
                    buttonYes.setOnClickListener {
                        changePassword()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun changePassword() {
        newPassword = binding.editNewPassword.text.toString().trim()
        confirmNewPassword = binding.editConfirmNewPassword.text.toString().trim()
        val lengthNewPassword = newPassword.length

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
        } else if (lengthNewPassword < 8) {
            alert(R.drawable.ic_warning, "Peringatan", "Password baru minimal 8 karakter.", R.color.red)
        } else if (newPassword != confirmNewPassword) {
            alert(R.drawable.ic_warning, "Peringatan", "Konfirmasi password baru tidak cocok.", R.color.red)
        } else {
            dialog.showProgressDialog(this)
            val client = ApiConfig.getApiService().changePassword(idUser, level, newPassword, token)
            client.enqueue(object : Callback<MetaResponse> {
                override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                    dialog.hideDialog()
                    val statusCode = response.body()?.meta?.code

                    if (response.isSuccessful) {
                        if (statusCode == "200") {
                            logout()
                            toast("Silahkan login kembali.")
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

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        HawkStorage.instance(this).deleteAll()
        finish()
    }

    companion object {
        const val TAG = "ChangePassword"
    }
}