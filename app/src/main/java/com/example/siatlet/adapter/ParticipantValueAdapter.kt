package com.example.siatlet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.activity.DetailCriteriaWeightActivity
import com.example.siatlet.activity.DetailParticipantValueActivity
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
                val idParticipantValue = idNilaiPeserta
                val idParticipant = idPeserta
                val nameContest = namaLomba
                val nameParticipant = namaPeserta
                val idContest = idLomba

                binding.apply {
                    textName.text = name
                    textProperty.text = "$value ($desc)"

                    cardParticipantValue.setOnClickListener {
                        val intent = Intent(context, DetailParticipantValueActivity::class.java)
                        intent.putExtra("id_participant_value", idParticipantValue)
                        intent.putExtra("name_contest", nameContest)
                        intent.putExtra("name_participant", nameParticipant)
                        intent.putExtra("id_participant", idParticipant)
                        intent.putExtra("id_contest", idContest)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = participantValueList.size
}