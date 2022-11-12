package com.example.siatlet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.activity.CriteriaValueActivity
import com.example.siatlet.activity.DetailCriteriaWeightActivity
import com.example.siatlet.databinding.ItemRowCriteriaByContestBinding
import com.example.siatlet.model.DataCriteria

class CriteriaByContestAdapter(private var criteriaList: List<DataCriteria>, private val context: Context): RecyclerView.Adapter<CriteriaByContestAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemRowCriteriaByContestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowCriteriaByContestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(criteriaList[position]) {
                val idCriteria = idKriteria
                val nameCriteria = namaKriteria
                val property = sifat
                val nameContest = namaLomba

                when (property) {
                    "Benefit" -> {
                        binding.textProperty.setTextColor(context.getColor(com.example.siatlet.R.color.green))
                    }
                    "Cost" -> {
                        binding.textProperty.setTextColor(context.getColor(com.example.siatlet.R.color.red))
                    }
                }

                binding.apply {
                    textName.text = nameCriteria
                    textProperty.text = property

                    cardCriteria.setOnClickListener {
                        val intent = Intent(context, CriteriaValueActivity::class.java)
                        intent.putExtra("id_criteria", idCriteria)
                        intent.putExtra("name_criteria", nameCriteria)
                        intent.putExtra("name_contest", nameContest)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount() = criteriaList.size
}