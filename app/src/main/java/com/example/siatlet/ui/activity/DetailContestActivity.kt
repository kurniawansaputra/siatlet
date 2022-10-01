package com.example.siatlet.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import com.example.siatlet.R
import com.example.siatlet.databinding.ActivityDetailContestBinding
import com.example.siatlet.databinding.LayoutDialogBinding
import com.example.siatlet.hawkstorage.HawkStorage
import com.example.siatlet.model.ContestByIdResponse
import com.example.siatlet.model.DataUserByLevel
import com.example.siatlet.model.MetaResponse
import com.example.siatlet.model.UserByLevelResponse
import com.example.siatlet.network.ApiConfig
import com.example.siatlet.util.DialogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailContestActivity : BaseActivity() {
    private lateinit var token: String
    private lateinit var idContest: String
    private lateinit var name: String
    private lateinit var date: String
    private lateinit var trainer: String
    private val level = "pelatih"

    private var idTrainer: String = ""
    private var nameTrainer: String = ""

    private lateinit var dialog: DialogUtil

    private lateinit var binding: ActivityDetailContestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailContestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setPref()
        setSpTrainer()
        setToolbar()
        setListener()
        setDetail()
    }

    private fun init() {
        dialog = DialogUtil()
    }

    private fun setPref() {
        val user = HawkStorage.instance(this).getUser()
        token = user.data?.token.toString()

        idContest = intent.getStringExtra("id_contest").toString()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }

            ivDelete.setOnClickListener {
                val binding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
                val builder: AlertDialog.Builder = AlertDialog.Builder(layoutInflater.context)
                builder.setView(binding.root)
                val dialog: AlertDialog = builder.create()
                binding.apply {
                    labelTitle.text = getString(R.string.title_delete)
                    labelMessage.text = getString(R.string.message_delete)
                    buttonNo.setOnClickListener {
                        dialog.dismiss()
                    }
                    buttonYes.setOnClickListener {
                        deleteContestById()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun setListener() {
        binding.apply {
            layoutUpdateContest.editDate.setOnClickListener {
                setDate()
            }

            layoutUpdateContest.buttonAddUpdate.text = getString(R.string.update)
            layoutUpdateContest.buttonAddUpdate.setOnClickListener {
                name = binding.layoutUpdateContest.editName.text.toString()
                date = binding.layoutUpdateContest.editDate.text.toString()

                val binding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
                val builder: AlertDialog.Builder = AlertDialog.Builder(layoutInflater.context)
                builder.setView(binding.root)
                val dialog: AlertDialog = builder.create()
                binding.apply {
                    labelTitle.text = getString(R.string.title_update)
                    labelMessage.text = getString(R.string.message_update)
                    buttonNo.setOnClickListener {
                        dialog.dismiss()
                    }
                    buttonYes.setOnClickListener {
                        updateContest()
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    private fun deleteContestById() {
        dialog.showProgressDialog(this)
        val client = ApiConfig.getApiService().deleteContest(token, idContest)
        client.enqueue(object : Callback<MetaResponse> {
            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                dialog.hideDialog()
                val statusCode = response.body()?.meta?.code

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        goToContest()
                        toast("Lomba berhasil dihapus.")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MetaResponse>, t: Throwable) {
                dialog.hideDialog()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun updateContest() {
        if (name.isEmpty() || date.isEmpty()) {
            alert(R.drawable.ic_warning, "Peringatan", "Harap lengkapi form terlebih dahulu.", R.color.red)
        } else {
            dialog.showProgressDialog(this)
            val client = ApiConfig.getApiService().updateContest(token, idContest, name, date, idTrainer)
            client.enqueue(object : Callback<MetaResponse> {
                override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                    dialog.hideDialog()
                    val statusCode = response.body()?.meta?.code

                    if (response.isSuccessful) {
                        if (statusCode == "200") {
                            goToContest()
                            toast("Lomba berhasil diupdate.")
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<MetaResponse>, t: Throwable) {
                    dialog.hideDialog()
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    private fun setDetail() {
        setLoading(true)
        val client = ApiConfig.getApiService().getContestById(token, idContest)
        client.enqueue(object : Callback<ContestByIdResponse> {
            override fun onResponse(call: Call<ContestByIdResponse>, response: Response<ContestByIdResponse>) {
                setLoading(false)
                val statusCode = response.body()?.meta?.code
                val responseBody = response.body()?.data

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        name = responseBody?.namaLomba.toString()
                        date = responseBody?.waktuLomba.toString()
                        trainer = responseBody?.idPelatih.toString()

                        binding.apply {
                            layoutUpdateContest.editName.setText(name)
                            layoutUpdateContest.editDate.setText(date)
                            layoutUpdateContest.spTrainer.id = trainer.toInt()
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ContestByIdResponse>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setSpTrainer() {
        val client = ApiConfig.getApiService().getUserByLevel(level)
        client.enqueue(object : Callback<UserByLevelResponse> {
            override fun onResponse(call: Call<UserByLevelResponse>, response: Response<UserByLevelResponse>) {
                val statusCode = response.body()?.meta?.code
                val message = response.body()?.meta?.message

                if (response.isSuccessful) {
                    if (statusCode == "200") {
                        val userByLevel: List<DataUserByLevel> = response.body()!!.data as List<DataUserByLevel>
                        val nameList: MutableList<String> = ArrayList()

                        for (i in userByLevel.indices) {
                            nameList.add(
                                userByLevel[i].nama.toString()
                            )
                        }
                        val trainerAdapter = ArrayAdapter(this@DetailContestActivity, R.layout.layout_dropdown, nameList)
                        binding.layoutUpdateContest.spTrainer.adapter = trainerAdapter

                        binding.layoutUpdateContest.spTrainer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                                idTrainer = userByLevel[position].idUser.toString()
                                nameTrainer = userByLevel[position].nama.toString()
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }
                    }
                } else {
                    Log.e(AddContestActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserByLevelResponse>, t: Throwable) {
                Log.e(AddContestActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setLoading(condition: Boolean) {
        if (condition) {
            binding.progressCircular.visibility = View.VISIBLE
            binding.containerLayout.visibility = View.GONE
        } else {
            binding.progressCircular.visibility = View.GONE
            binding.containerLayout.visibility = View.VISIBLE
        }
    }

    private fun goToContest() {
        val intent = Intent(this, ContestActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun setDate() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _: DatePicker, i: Int, i1: Int, i2: Int ->
            binding.layoutUpdateContest.editDate.setText("$i-${i1 + 1}-$i2")
        }, year, month, day).show()
    }

    companion object {
        const val TAG = "DetailContest"
    }
}