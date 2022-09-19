package com.example.siatlet.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityDetailUserBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : BaseActivity() {
    private lateinit var idUser: String
    private lateinit var token: String

    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPref()
        setToolbar()
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
                    labelTitle.text = "Hapus Data"
                    labelMessage.text = "Apakah anda yakin ingin menghapus data ini?"
                    buttonNo.setOnClickListener {
                        dialog.dismiss()
                    }
                    buttonYes.setOnClickListener {
                        deleteUserById()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun  deleteUserById() {
//        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().deleteUser(token, idUser)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
//                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToUser()
                        toast("User berhasil dihapus.")
                    }
                } else {
                    Log.e(AddUserActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MetaResponse>, t: Throwable) {
//                dialog.hideDialog()
                Log.e(AddUserActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun goToUser() {
        val intent = Intent(this, UserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}