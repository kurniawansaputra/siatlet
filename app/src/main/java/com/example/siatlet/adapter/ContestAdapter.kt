package com.example.siatlet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.databinding.ItemRowContestBinding
import com.example.siatlet.model.DataContest
import com.example.siatlet.ui.activity.DetailContestActivity

class ContestAdapter(private var contestList: List<DataContest>, private val context: Context): RecyclerView.Adapter<ContestAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemRowContestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowContestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(contestList[position]) {
                val name = namaLomba
                val date = waktuLomba
                val idContest = idLomba
                val trainer = idPelatih

                binding.apply {
                    textName.text = name
                    textTrainer.text = trainer
                    textDate.text = date

                    cardContest.setOnClickListener {
                        val intent = Intent(context, DetailContestActivity::class.java)
                        intent.putExtra("id_contest", idContest)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = contestList.size
}