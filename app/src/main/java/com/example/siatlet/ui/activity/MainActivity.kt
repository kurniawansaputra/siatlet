package com.example.siatlet.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityMainBinding
import com.example.siatlet.hawkstorage.HawkStorage

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDetail()
        setMenu()
        setListener()

    }

    private fun setDetail() {
        val user = HawkStorage.instance(this).getUser()
        val name = user.data?.nama

        binding.apply {
            textName.text = "Selamat datang, $name!"
        }
    }

    private fun setMenu() {
        binding.apply {
            containerUser.labelTitle.text = "User"
            containerUser.ivIcon.setImageResource(R.drawable.ic_user)

            containerParticipant.labelTitle.text = "Peserta"
            containerParticipant.ivIcon.setImageResource(R.drawable.ic_participation)

            containerContest.labelTitle.text = "Lomba"
            containerContest.ivIcon.setImageResource(R.drawable.ic_contest)
        }
    }

    private fun setListener() {
        binding.apply {
            containerUser.cardMenu.setOnClickListener {
                val intent = Intent(this@MainActivity, UserActivity::class.java)
                startActivity(intent)
            }

            containerContest.cardMenu.setOnClickListener {
                val intent = Intent(this@MainActivity, ContestActivity::class.java)
                startActivity(intent)
            }

            labelLogout.setOnClickListener {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                HawkStorage.instance(this@MainActivity).deleteAll()
            }
        }
    }
}