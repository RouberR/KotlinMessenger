package com.rouber.kotlinmessenger

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.rouber.kotlinmessenger.messages.LatestMessagesActivity
import com.rouber.kotlinmessenger.models.User
import com.rouber.kotlinmessenger.registerlogin.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            performRegister()
        }


        already_have_account_textview.setOnClickListener {
            Log.d("MainActivity", "Click ")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        photo_button_register.setOnClickListener {
            Log.d("RegisterActivity", "Click button photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)  //разобраться с registerForActivityResult

        }

    }

    var selectedPhotoUri: Uri? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Log.d("RegisterActivity", "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)


            selectphoto_image_register.setImageBitmap(bitmap)
                photo_button_register.alpha = 0f

            //  val bitmapDrawable = BitmapDrawable(bitmap)
         //   photo_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {


        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        Log.d("MainActivity", "Email is $email")
        Log.d("MainActivity", "Password is $password")

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста ведите Email и Password", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d(
                    "RegisterActivity",
                    "Пользователь создан успешно! id =  ${it.result!!.user!!.uid}"
                )

                uploadImageToFirebaseStorage()

            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Ошибка при создание пользователя ${it.message}")
                Toast.makeText(
                    this, "Ошибка при создание пользователя ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { it ->
                Log.d("RegisterActivity", "Успешно добавлено изображение ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File location $it")

                    saveUserToFiredaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {

            }
    }

    private fun saveUserToFiredaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_edittext_register.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Пользователь сохранен в базе данных")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}
