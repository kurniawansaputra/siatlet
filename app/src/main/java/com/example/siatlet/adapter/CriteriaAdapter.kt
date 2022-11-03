package com.example.siatlet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.R
import com.example.siatlet.databinding.ItemRowCriteriaBinding
import com.example.siatlet.model.DataCriteria
import com.example.siatlet.activity.DetailCriteriaActivity

class CriteriaAdapter(private var criteriaList: List<DataCriteria>, private val context: Context): RecyclerView.Adapter<CriteriaAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemRowCriteriaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowCriteriaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(criteriaList[position]) {
                val idCriteria = idKriteria
                val name = namaKriteria
                val property = sifat
                val contest = namaLomba

                when (property) {
                    "Benefit" -> {
                        binding.textProperty.setTextColor(context.getColor(R.color.green))
                    }
                    "Cost" -> {
                        binding.textProperty.setTextColor(context.getColor(R.color.red))
                    }
                }

                binding.apply {
                    textName.text = name
                    textProperty.text = property
                    textContest.text = contest

                    cardCriteria.setOnClickListener {
                        val intent = Intent(context, DetailCriteriaActivity::class.java)
                        intent.putExtra("id_criteria", idCriteria)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = criteriaList.size
}