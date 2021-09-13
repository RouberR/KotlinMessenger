package com.rouber.kotlinmessenger.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.core.view.View
import com.rouber.kotlinmessenger.NewMessagesActivuty
import com.rouber.kotlinmessenger.R
import com.rouber.kotlinmessenger.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)


        //val username = intent.getStringExtra(NewMessagesActivuty.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewMessagesActivuty.USER_KEY)
        supportActionBar?.title = user?.username

        val adapter = GroupAdapter<ViewHolder>()


        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatToItem())
        adapter.add(ChatToItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatFromItem())



        rectclerview_chat_log.adapter = adapter
    }
}

class ChatFromItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
    return R.layout.chat_from_row
    }
}


class ChatToItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}