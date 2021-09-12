package com.rouber.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_messages_activuty.*
import kotlinx.android.synthetic.main.user_row_new_messager.view.*
import java.util.*

class NewMessagesActivuty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_messages_activuty)

        supportActionBar?.title = "Выберите пользователя"

       /* val adapter = GroupAdapter<ViewHolder>()

        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())
        recyclerview_newmesseges.adapter = adapter
        */
        fetchUsers()
    }

    private fun fetchUsers(){
       val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
            snapshot.children.forEach {
                Log.d("NewMesseger", it.toString())

                val user = it.getValue(User::class.java)
                if (user != null){
                    adapter.add(UserItem(user))
                }

            }
                recyclerview_newmesseges.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}

class UserItem(val user: User): Item<ViewHolder>(){



    override fun getLayout(): Int {
        return R.layout.user_row_new_messager
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_messager.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_messeger)
    }
}