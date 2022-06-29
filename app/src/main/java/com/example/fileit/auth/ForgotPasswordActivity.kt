package com.example.fileit.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fileit.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth :FirebaseAuth
    private lateinit var u_email : EditText
    private lateinit var submitForgotEmailBt : Button
    private lateinit var backForgotEmailBt : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        u_email = findViewById(R.id.forgot_email_ed)
        submitForgotEmailBt = findViewById(R.id.submitForgotButton)
        backForgotEmailBt = findViewById(R.id.backForgotButton)

        backForgotEmailBt.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        submitForgotEmailBt.setOnClickListener{
            var email = u_email.text.toString()
            if (TextUtils.isEmpty(email)){
                Toast.makeText(this,"Please fill in the field",Toast.LENGTH_LONG).show()
            }else{
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this,"Reset link have been sent to $email",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,"Failed to send reset link to $email",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}