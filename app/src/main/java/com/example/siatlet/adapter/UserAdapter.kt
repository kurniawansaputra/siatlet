package com.example.siatlet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.databinding.ItemRowUserBinding
import com.example.siatlet.model.DataItemUser

class UserAdapter(private var userList: List<DataItemUser>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var nameLevel: String

    class ViewHolder (val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(userList[position]) {
                val name = nama

                when (level) {
                    "1" -> {
                        nameLevel = "Admin"
                    }
                    "2" -> {
                        nameLevel = "Pelatih"
                    }
                    "3" -> {
                        nameLevel = "Peserta"
                    }
                }

                binding.apply {
                    textName.text = name
                    textLevel.text = nameLevel
                }
            }
        }
    }

    override fun getItemCount(): Int = userList.size

}