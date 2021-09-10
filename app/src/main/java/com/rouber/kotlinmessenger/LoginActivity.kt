package com.rouber.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_text_login.text.toString()
            val password = password_text_login.text.toString();
            Log.d("LoginActivity", "Email is $email\n Password is $password")
        }
        back_regiscter_text.setOnClickListener {
            finish()
        }


    }
}