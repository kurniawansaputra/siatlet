package com.example.siatlet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.R
import com.example.siatlet.databinding.ItemRowParticipantBinding
import com.example.siatlet.model.DataParticipant

class ParticipantAdapter(private var participantList: List<DataParticipant>, private val context: Context): RecyclerView.Adapter<ParticipantAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemRowParticipantBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(participantList[position]) {
                val name = namaPeserta
                val numberReg = noReg

                when (jenisKelamin) {
                    "laki-laki" -> {
                        binding.ivGender.setImageResource(R.drawable.ic_male)
                    }
                    "perempuan" -> {
                        binding.ivGender.setImageResource(R.drawable.ic_female)
                    }
                }

                binding.apply {
                    textName.text = name
                    textNumberReg.text = numberReg
                }
            }
        }
    }

    override fun getItemCount(): Int = participantList.size
}