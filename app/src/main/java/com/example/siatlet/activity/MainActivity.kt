package com.example.siatlet.activity

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

            when (level) {
                "admin" -> {
                    containerContestByTrainer.cardMenu.visibility = View.GONE
//                    containerScore.cardMenu.visibility = View.GONE
                }
                "pelatih" -> {
                    containerUser.cardMenu.visibility = View.GONE
                    containerContest.cardMenu.visibility = View.GONE
                    containerCriteria.cardMenu.visibility = View.GONE
                }
                "pemilik" -> {
//                    containerScore.cardMenu.visibility = View.GONE
                    containerUser.cardMenu.visibility = View.GONE
                    containerContest.cardMenu.visibility = View.GONE
                    containerCriteria.cardMenu.visibility = View.GONE
                }
            }
        }
    }

    private fun setMenu() {
        binding.apply {
            containerUser.labelTitle.text = "User"
            containerUser.ivIcon.setImageResource(R.drawable.ic_user)

            containerContestByTrainer.labelTitle.text = "Lomba"
            containerContestByTrainer.ivIcon.setImageResource(R.drawable.ic_contest)

            containerContest.labelTitle.text = "Lomba"
            containerContest.ivIcon.setImageResource(R.drawable.ic_contest)

            containerCriteria.labelTitle.text = "Kriteria"
            containerCriteria.ivIcon.setImageResource(R.drawable.ic_criteria)

//            containerScore.labelTitle.text = "Nilai Peserta"
//            containerScore.ivIcon.setImageResource(R.drawable.ic_score)
//
//            containerRanking.labelTitle.text = "Perankingan"
//            containerRanking.ivIcon.setImageResource(R.drawable.ic_ranking)
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
            containerContestByTrainer.cardMenu.setOnClickListener {
                val intent = Intent(this@MainActivity, ContestByTrainerActivity::class.java)
                startActivity(intent)
            }
        }
    }
}