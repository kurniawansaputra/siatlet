package com.example.siatlet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siatlet.R
import com.example.siatlet.databinding.ItemRowUserBinding
import com.example.siatlet.model.DataItemUser
import com.example.siatlet.ui.activity.DetailUserActivity

class UserAdapter(private var userList: List<DataItemUser>, private val context: Context): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
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
                val gender = jenisKelamin
                val username = username

                when (level) {
                    "1" -> {
                        nameLevel = "Admin"
                        binding.cardLevel.setCardBackgroundColor(context.getColor(R.color.green))
                    }
                    "2" -> {
                        nameLevel = "Pelatih"
                        binding.cardLevel.setCardBackgroundColor(context.getColor(R.color.purple))
                    }
                    "3" -> {
                        nameLevel = "Pemilik"
                        binding.cardLevel.setCardBackgroundColor(context.getColor(R.color.blue))
                    }
                }

                when (gender) {
                    "1" -> {
                        binding.ivGender.setImageResource(R.drawable.ic_male)
                    }
                    "2" -> {
                        binding.ivGender.setImageResource(R.drawable.ic_female)
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