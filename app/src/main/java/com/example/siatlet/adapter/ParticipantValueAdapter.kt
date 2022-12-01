package com.example.siatlet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.databinding.ItemRowParticipantValueBinding
import com.example.siatlet.model.DataParticipantValue

class ParticipantValueAdapter(private var participantValueList: List<DataParticipantValue>, private val context: Context): RecyclerView.Adapter<ParticipantValueAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemRowParticipantValueBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowParticipantValueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(participantValueList[position]) {
                val name = namaKriteria
                val value = nilaiKriteriaPeserta
                val desc = ketNilaiKriteria

                binding.apply {
                    textName.text = name
                    textProperty.text = "$value ($desc)"
                }
            }
        }
    }

    override fun getItemCount(): Int = participantValueList.size
}