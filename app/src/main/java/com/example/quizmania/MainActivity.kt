package com.example.quizmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn)
        val Uname = findViewById<EditText>(R.id.editTextText)


        btn.setOnClickListener{

            val username = Uname.text.toString()
            if(username.isNotBlank()) {
                val i = Intent(this, Quiz_screen::class.java)
                i.putExtra("name",username)
                startActivity(i)
            }
            else if (username.equals("")){
                Toast.makeText(this, "enter any name", Toast.LENGTH_LONG).show()
            }

        }
    }
}