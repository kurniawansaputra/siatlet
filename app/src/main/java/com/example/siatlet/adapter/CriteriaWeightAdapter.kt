package com.example.siatlet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.activity.CriteriaWeightActivity
import com.example.siatlet.activity.DetailCriteriaWeightActivity
import com.example.siatlet.databinding.ItemRowCriteriaWeightBinding
import com.example.siatlet.model.DataCriteriaWeightByContest

class CriteriaWeightAdapter(private var criteriaWeightList: List<DataCriteriaWeightByContest>, private val context: Context): RecyclerView.Adapter<CriteriaWeightAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemRowCriteriaWeightBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowCriteriaWeightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(criteriaWeightList[position]) {
                val idCriteriaWeight = idBobot
                val idContest = idLomba
                val nameContest = namaLomba
                val nameCriteriaWeight = namaKriteria
                val weight = bobot

                binding.apply {
                    textName.text = nameCriteriaWeight
                    textWeight.text = weight

                    cardCriteriaWeight.setOnClickListener {
                        val intent = Intent(context, DetailCriteriaWeightActivity::class.java)
                        intent.putExtra("id_criteria_weight", idCriteriaWeight)
                        intent.putExtra("id_contest", idContest)
                        intent.putExtra("name_contest", nameContest)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = criteriaWeightList.size
}