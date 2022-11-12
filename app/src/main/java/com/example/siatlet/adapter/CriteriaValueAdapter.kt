package com.example.siatlet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.R
import com.example.siatlet.activity.DetailCriteriaValueActivity
import com.example.siatlet.activity.DetailCriteriaWeightActivity
import com.example.siatlet.databinding.ItemRowCriteriaValueBinding
import com.example.siatlet.model.DataCriteriaValue

class CriteriaValueAdapter(private var criteriaValueList: List<DataCriteriaValue>, private val context: Context): RecyclerView.Adapter<CriteriaValueAdapter.ViewHolder>(){
    class ViewHolder (val binding: ItemRowCriteriaValueBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowCriteriaValueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(criteriaValueList[position]) {
                val idCriteriaValue = idNilaiKriteria
                val idCriteria = idKriteria
                val nameCriteria = namaKriteria
//                val nameContest = namaLomba

                val value = nilai
                val desc = keterangan
                val gender = jenisKelamin

                when (gender) {
                    "laki-lai" -> {
                        binding.ivValue.setImageResource(R.drawable.ic_value_male)
                    }
                    "perempuan" -> {
                        binding.ivValue.setImageResource(R.drawable.ic_value_female)
                    }
                }


                binding.apply {
                    textValue.text = value
                    textDesc.text = desc
                    textGender.text = gender

                    cardCriteriaValue.setOnClickListener {
                        val intent = Intent(context, DetailCriteriaValueActivity::class.java)
                        intent.putExtra("id_criteria_value", idCriteriaValue)
                        intent.putExtra("id_criteria", idCriteria)
                        intent.putExtra("name_criteria", nameCriteria)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount() = criteriaValueList.size
}