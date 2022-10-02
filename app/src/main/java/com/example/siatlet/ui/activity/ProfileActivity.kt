package com.example.siatlet.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityProfileBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        setDetail()
        setListener()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun setDetail() {
        val user = HawkStorage.instance(this).getUser()
        val name = user.data?.nama.toString()
        val username = user.data?.username.toString()
        val level = user.data?.level.toString()

        binding.apply {
            textName.text = name
            textUsername.text = username

            when (level) {
                "admin" -> {
                    textLevel.text = "Admin"
                    binding.cardLevel.setCardBackgroundColor(getColor(R.color.green))
                }
                "pelatih" -> {
                    textLevel.text = "Pelatih"
                    binding.cardLevel.setCardBackgroundColor(getColor(R.color.purple))
                }
                "pemilik" -> {
                    textLevel.text = "Pemilik"
                    binding.cardLevel.setCardBackgroundColor(getColor(R.color.blue))
                }
            }
        }
    }

    private fun setListener() {
        binding.apply {
            buttonChangePassword.setOnClickListener {
                val intent = Intent(this@ProfileActivity, ChangePasswordActivity::class.java)
                startActivity(intent)
            }

            buttonLogout.setOnClickListener {
                val binding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
                val builder: AlertDialog.Builder = AlertDialog.Builder(layoutInflater.context)
                builder.setView(binding.root)
                val dialog: AlertDialog = builder.create()
                binding.apply {
                    labelTitle.text = "Keluar"
                    labelMessage.text = "Apakah anda yakin ingin keluar dari aplikasi ini?"
                    buttonNo.setOnClickListener {
                        dialog.dismiss()
                    }
                    buttonYes.setOnClickListener {
                        logout()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        HawkStorage.instance(this).deleteAll()
        finish()
    }
}