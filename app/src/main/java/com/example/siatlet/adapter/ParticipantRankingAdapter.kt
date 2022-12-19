package com.example.siatlet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.databinding.ItemRowParticipantRankingBinding
import com.example.siatlet.model.DataParticipantRanking

class ParticipantRankingAdapter(private var participantRankList: List<DataParticipantRanking>, private val context: Context): RecyclerView.Adapter<ParticipantRankingAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemRowParticipantRankingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowParticipantRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(participantRankList[position]) {
                val rank = ranking
                val result = hasil
                val name = namaPeserta

                binding.apply {
                    textName.text = name
                    textRanking.text = rank.toString()
                    textDesc.text = result
                }
            }
        }
    }

    override fun getItemCount(): Int = participantRankList.size
}