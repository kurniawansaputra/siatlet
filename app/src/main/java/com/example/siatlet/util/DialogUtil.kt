package com.example.siatlet.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.siatlet.R

class DialogUtil {
    private var dialogBuilder: AlertDialog? = null

    fun showProgressDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_loading, null)
        dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        dialogBuilder?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogBuilder?.show()
    }

    fun hideDialog() {
        if (dialogBuilder != null){
            dialogBuilder?.dismiss()
        }
    }
}