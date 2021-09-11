package com.rouber.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        login_button_login.setOnClickListener {
            performLogin()
        }


        back_regiscter_text.setOnClickListener {
            finish()
        }


    }


    private fun performLogin(){
            val email = email_text_login.text.toString()
            val password = password_text_login.text.toString();
            Log.d("LoginActivity", "Email is $email\n Password is $password")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Main", "Пользователь успешно вошел! id =  ${it.result!!.user!!.uid}")
                }
                .addOnFailureListener{
                    Log.d("Main", "Ошибка при входе пользователя ${it.message}")
                    Toast.makeText(this, "Ошибка при входе пользователя ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }
}