package com.example.siatlet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.R
import com.example.siatlet.databinding.ItemRowUserBinding
import com.example.siatlet.model.DataUser
import com.example.siatlet.activity.DetailUserActivity

class UserAdapter(private var userList: List<DataUser>, private val context: Context): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var nameLevel: String

    class ViewHolder (val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(userList[position]) {
                val idUser = idUser
                val name = nama
                val username = username

                when (level) {
                    "admin" -> {
                        nameLevel = "Admin"
                        binding.cardLevel.setCardBackgroundColor(context.getColor(R.color.green))
                    }
                    "pelatih" -> {
                        nameLevel = "Pelatih"
                        binding.cardLevel.setCardBackgroundColor(context.getColor(R.color.purple))
                    }
                    "pemilik" -> {
                        nameLevel = "Pemilik"
                        binding.cardLevel.setCardBackgroundColor(context.getColor(R.color.blue))
                    }
                }

                binding.apply {
                    textName.text = name
                    textUsername.text = username
                    textLevel.text = nameLevel
                    cardUser.setOnClickListener {
                        val intent = Intent(context, DetailUserActivity::class.java)
                        intent.putExtra("id_user", idUser)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = userList.size

}