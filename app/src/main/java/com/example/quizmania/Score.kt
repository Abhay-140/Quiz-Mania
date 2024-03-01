package com.example.quizmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.example.quizmania.R.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Score : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_score)

        val fbaseRef = FirebaseDatabase.getInstance().getReference("users")
        val text = findViewById<TextView>(id.textView6)
        val imgbtn = findViewById<ImageButton>(id.imageButton)
        val sc = intent.getIntExtra("score",0)
        val user = intent.getStringExtra("name")

        if (user != null) {
            fbaseRef.child(user).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userScore = snapshot.getValue(Long::class.java)
                        if (userScore != null && userScore < sc.toLong()) {
                            fbaseRef.child(user).setValue(sc)
                        }
                    }
                    else{
                        fbaseRef.child(user).setValue(sc)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        text.text = "$user scored $sc out of 10 "

        imgbtn.setOnClickListener {
            val i = Intent(this,MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        }


    }
}