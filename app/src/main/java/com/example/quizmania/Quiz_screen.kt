package com.example.quizmania

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmania.databinding.ActivityQuizScreenBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Quiz_screen : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var binding: ActivityQuizScreenBinding
    var score = 0
    var index = 0
    var answer=""
    val myList = quesList()
    var init = true
    private lateinit var countDownTimer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityQuizScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val uname = intent.getStringExtra("name")
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("questions")

        if (init) {
            initialise(myList[index]) { ans ->
                answer = ans
                index += 1
            }
            init = false
        }
         countDownTimer = object : CountDownTimer(16000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                if (secondsLeft < 10 ){
                    val txt = "   00:0$secondsLeft   "
                    binding.textView4.text = txt
                }
                else{
                    val txt = "   00:$secondsLeft   "
                    binding.textView4.text = txt
                }
            }

            override fun onFinish() {
                if (index < myList.size) {
                    initialise(myList[index]) { ans ->
                        // Here you can use the 'ans' value after it's retrieved
                        // This code is executed once the data retrieval is complete
                        index += 1
                        binding.radioButton5.isChecked = true
                        Log.d("checking","$score  $index  $answer")
                        answer = ans
                        binding.textView3.text = index.toString()
                        cancel()
                        start()
                    }

                } else {
                    val i = Intent(applicationContext,Score::class.java)
                    i.putExtra("score",score)
                    i.putExtra("name",uname)
                    Log.d("checking","$score  $index  $answer")
                    score = 0
                    index = 0
                    answer=""
                    startActivity(i)
                    finish()

                }


            }
        }


        countDownTimer.start()   // Start the countdown







        binding.button.setOnClickListener {
            if (binding.radioButton.text.equals(answer)  && binding.radioButton.isChecked){
                score++
            }
            else if (binding.radioButton2.text.equals(answer)  && binding.radioButton2.isChecked) {
                score++
            }
            else if (binding.radioButton3.text.equals(answer)  && binding.radioButton3.isChecked){
                score++
            }
            else if (binding.radioButton4.text.equals(answer)  && binding.radioButton4.isChecked){
                score++
            }



            if (binding.radioButton.isChecked || binding.radioButton2.isChecked || binding.radioButton3.isChecked || binding.radioButton4.isChecked) {

                if (index < myList.size) {
                    initialise(myList[index]) { ans ->
                        // Here you can use the 'ans' value after it's retrieved
                        // This code is executed once the data retrieval is complete
                        binding.radioButton5.isChecked = true
                        index += 1
                        Log.d("checking","$score  $index  $answer")
                        answer = ans
                        binding.textView3.text = index.toString()
                        countDownTimer.cancel()
                        countDownTimer.start()

                    }

                } else {

                    val i = Intent(this,Score::class.java)
                    i.putExtra("score",score)
                    i.putExtra("name",uname)
                    Log.d("checking","$score  $index  $answer")
                    score = 0
                    index = 0
                    answer=""
                    startActivity(i)
                    finish()
                }
            }

        }



    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the timer to stop it when the activity is destroyed
        countDownTimer?.cancel()
    }


    private fun initialise(path: String, callback: (String) -> Unit) {
        reference.child(path).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                binding.radioButton.text = dataSnapshot.child("a").value.toString()
                binding.radioButton2.text = dataSnapshot.child("b").value.toString()
                binding.radioButton3.text = dataSnapshot.child("c").value.toString()
                binding.radioButton4.text = dataSnapshot.child("d").value.toString()
                binding.textView2.text = dataSnapshot.child("ques").value.toString()
                val ans = dataSnapshot.child("ans").value.toString()

                callback(ans)
            } else {
                Toast.makeText(this, "This question is invalid", Toast.LENGTH_LONG).show()
                callback("") // Handle the absence of data as needed
            }
        }
    }



    private fun quesList():List<String> {

        val set = mutableSetOf<String>()
        while(set.size != 10){
            val random = (1..20).random()
            set.add(random.toString())
        }

        return set.toList()
    }
}

private fun <E> List<E>.clear() {
    clear()
}

