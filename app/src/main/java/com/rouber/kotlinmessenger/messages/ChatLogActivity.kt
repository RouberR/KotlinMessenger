package com.rouber.kotlinmessenger.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rouber.kotlinmessenger.NewMessagesActivuty
import com.rouber.kotlinmessenger.R
import com.rouber.kotlinmessenger.models.ChatMessage
import com.rouber.kotlinmessenger.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        rectclerview_chat_log.adapter = adapter





        //val username = intent.getStringExtra(NewMessagesActivuty.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewMessagesActivuty.USER_KEY)
        supportActionBar?.title = user?.username

        //setupDummyData()
    listenForMessages()

        send_button_chat_log.setOnClickListener {
            performSentMessage()
        }

    }

    private fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
               val chatMessage =  p0.getValue(ChatMessage::class.java)

                if (chatMessage !=null) {

                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(ChatToItem(chatMessage.text))
                    } else {
                        adapter.add(ChatFromItem(chatMessage.text))
                    }
                }


            }
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })
    }



    private fun performSentMessage(){

        val text = edittext_chat_log.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessagesActivuty.USER_KEY)
        val toId = user!!.uid

        if (fromId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000 )
            reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d("Tag", "Saved our chat message $reference.key")
                }
    }



        private fun setupDummyData() {
            val adapter = GroupAdapter<ViewHolder>()


            adapter.add(ChatFromItem("From messedge"))
            adapter.add(ChatToItem("To messedge"))



            rectclerview_chat_log.adapter = adapter
        }




}

class ChatFromItem(val text: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = text
    }
    override fun getLayout(): Int {
    return R.layout.chat_from_row
    }
}


class ChatToItem(val text: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = text
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}