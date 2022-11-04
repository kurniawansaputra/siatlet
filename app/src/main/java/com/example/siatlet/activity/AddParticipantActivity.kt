package com.example.siatlet.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.databinding.ActivityAddParticipantBinding

class AddParticipantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}