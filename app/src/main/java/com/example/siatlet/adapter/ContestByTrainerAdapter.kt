package com.example.siatlet.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.activity.*
import com.example.siatlet.databinding.ItemRowContestByTrainerBinding
import com.example.siatlet.databinding.LayoutDialogContestBinding
import com.example.siatlet.model.DataContest

class ContestByTrainerAdapter(private var contestByTrainerList: List<DataContest>, private val context: Context): RecyclerView.Adapter<ContestByTrainerAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemRowContestByTrainerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowContestByTrainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(contestByTrainerList[position]) {
                val name = namaLomba
                val date = waktuLomba
                val idContest = idLomba

                binding.apply {
                    textName.text = name
                    textDate.text = date

                    cardContest.setOnClickListener {
                        val binding: LayoutDialogContestBinding = LayoutDialogContestBinding.inflate(LayoutInflater.from(it.rootView.context))
                        val builder: AlertDialog.Builder = AlertDialog.Builder(it.rootView.context)
                        builder.setView(binding.root)
                        val dialog: AlertDialog = builder.create()
                        binding.apply {
                            labelParticipant.setOnClickListener {
                                val intent = Intent(context, ParticipantActivity::class.java)
                                intent.putExtra("id_contest", idContest)
                                intent.putExtra("name_contest", name)
                                context.startActivity(intent)
                                dialog.dismiss()
                            }

                            labelCriteriaWeight.setOnClickListener {
                                val intent = Intent(context, CriteriaWeightActivity::class.java)
                                intent.putExtra("id_contest", idContest)
                                intent.putExtra("name_contest", name)
                                context.startActivity(intent)
                                dialog.dismiss()
                            }

                            labelCriteriaValue.setOnClickListener {
                                val intent = Intent(context, CriteriaByContestActivity::class.java)
                                intent.putExtra("id_contest", idContest)
                                intent.putExtra("name_contest", name)
                                context.startActivity(intent)
                                dialog.dismiss()
                            }

                            labelRanked.setOnClickListener {
                                val intent = Intent(context, RankedActivity::class.java)
                                intent.putExtra("id_contest", idContest)
                                intent.putExtra("name_contest", name)
                                context.startActivity(intent)
                                dialog.dismiss()
                            }

                            labelBack.setOnClickListener {
                                dialog.dismiss()
                            }
                        }
                        dialog.setCancelable(true)
                        dialog.show()
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = contestByTrainerList.size
}