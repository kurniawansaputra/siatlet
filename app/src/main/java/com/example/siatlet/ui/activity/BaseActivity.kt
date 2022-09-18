package com.example.siatlet.ui.activity

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.siatlet.R
import com.tapadoo.alerter.Alerter

open class BaseActivity: AppCompatActivity() {
    fun alert(icon: Int, title: String, description: String, color: Int) {
        Alerter.create(this)
            .setIcon(icon)
            .setTitle(title)
            .setText(description)
            .enableSwipeToDismiss()
            .setBackgroundColorRes(color)
            .setTitleAppearance(R.style.TextView_PoppinsMedium16spWhite)
            .setTextAppearance(R.style.TextView_PoppinsRegular14spWhite)
            .show()
    }

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}