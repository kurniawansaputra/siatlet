package com.example.siatlet.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        val level = user.data?.level

        binding.apply {
            textName.text = "Selamat datang, $name!"

            if (level == "admin") {
                containerParticipant.cardMenu.visibility = View.GONE
                containerCriteriaValue.cardMenu.visibility = View.GONE
                containerScore.cardMenu.visibility = View.GONE
            } else if (level == "pelatih") {
                containerUser.cardMenu.visibility = View.GONE
                containerContest.cardMenu.visibility = View.GONE
                containerCriteria.cardMenu.visibility = View.GONE
            }
        }
    }

    private fun setMenu() {
        binding.apply {
            containerUser.labelTitle.text = "User"
            containerUser.ivIcon.setImageResource(R.drawable.ic_user)

            containerParticipant.labelTitle.text = "Peserta"
            containerParticipant.ivIcon.setImageResource(R.drawable.ic_participant)

            containerContest.labelTitle.text = "Lomba"
            containerContest.ivIcon.setImageResource(R.drawable.ic_contest)

            containerCriteria.labelTitle.text = "Kriteria"
            containerCriteria.ivIcon.setImageResource(R.drawable.ic_criteria)

            containerCriteriaValue.labelTitle.text = "Bobot Kriteria"
            containerCriteriaValue.ivIcon.setImageResource(R.drawable.ic_criteria)

            containerScore.labelTitle.text = "Nilai Peserta"
            containerScore.ivIcon.setImageResource(R.drawable.ic_score)

            containerRanking.labelTitle.text = "Perankingan"
            containerRanking.ivIcon.setImageResource(R.drawable.ic_ranking)
        }
    }

    private fun setListener() {
        binding.apply {
            appBarLayout.setOnClickListener {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }

            containerUser.cardMenu.setOnClickListener {
                val intent = Intent(this@MainActivity, UserActivity::class.java)
                startActivity(intent)
            }

            containerContest.cardMenu.setOnClickListener {
                val intent = Intent(this@MainActivity, ContestActivity::class.java)
                startActivity(intent)
            }

            containerCriteria.cardMenu.setOnClickListener {
                val intent = Intent(this@MainActivity, CriteriaActivity::class.java)
                startActivity(intent)
            }

            containerParticipant.cardMenu.setOnClickListener {
                val intent = Intent(this@MainActivity, ParticipantActivity::class.java)
                startActivity(intent)
            }
        }
    }
}