package com.example.siatlet.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.R
import com.example.siatlet.activity.*
import com.example.siatlet.databinding.ItemRowParticipantBinding
import com.example.siatlet.databinding.LayoutDialogContestBinding
import com.example.siatlet.databinding.LayoutDialogParticipantBinding
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
                val idContest = idLomba
                val nameContest = namaLomba
                val idParticipant = idPeserta
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

                    cardParticipant.setOnClickListener {
                        val binding: LayoutDialogParticipantBinding = LayoutDialogParticipantBinding.inflate(LayoutInflater.from(it.rootView.context))
                        val builder: AlertDialog.Builder = AlertDialog.Builder(it.rootView.context)
                        builder.setView(binding.root)
                        val dialog: AlertDialog = builder.create()
                        binding.apply {
                            labelDetailParticipant.setOnClickListener {
                                val intent = Intent(context, DetailParticipantActivity::class.java)
                                intent.putExtra("id_participant", idParticipant)
                                intent.putExtra("id_contest", idContest)
                                intent.putExtra("name_contest", nameContest)
                                context.startActivity(intent)
                                dialog.dismiss()
                            }

                            labelParticipantValue.setOnClickListener {
                                val intent = Intent(context, ParticipantValueActivity::class.java)
                                intent.putExtra("id_participant", idParticipant)
                                intent.putExtra("id_contest", idContest)
                                intent.putExtra("name_contest", nameContest)
                                intent.putExtra("name", name)
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

    override fun getItemCount(): Int = participantList.size
}