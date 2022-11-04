package com.example.siatlet.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.siatlet.databinding.ActivityDetailParticipantBinding
import com.example.siatlet.databinding.ActivityDetailUserBinding

class DetailParticipantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}