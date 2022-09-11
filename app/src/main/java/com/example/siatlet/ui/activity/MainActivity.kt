package com.example.siatlet.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.siatlet.databinding.ActivityMainBinding
import com.example.siatlet.hawkstorage.HawkStorage

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = HawkStorage.instance(this).getUser()
        val name = user.data?.nama

        binding.apply {
            textName.text = "Selamat datang, $name!"

            labelLogout.setOnClickListener {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                HawkStorage.instance(this@MainActivity).deleteAll()
            }
        }
    }
}