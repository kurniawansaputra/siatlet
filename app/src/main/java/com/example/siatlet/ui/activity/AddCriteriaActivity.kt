package com.example.siatlet.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.siatlet.databinding.ActivityAddContestBinding
import com.example.siatlet.databinding.ActivityAddCriteriaBinding

class AddCriteriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCriteriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCriteriaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}