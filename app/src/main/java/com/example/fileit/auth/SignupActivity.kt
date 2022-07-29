package com.example.fileit.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fileit.MainPage
import com.example.fileit.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth :FirebaseAuth
    private lateinit var u_email : EditText
    private lateinit var u_password : EditText
    private lateinit var loginButton : Button
    private lateinit var signupButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        u_email = findViewById(R.id.signup_email_ed)
        u_password = findViewById(R.id.signup_pass_ed)

        loginButton = findViewById(R.id.loginButton)
        signupButton = findViewById(R.id.signupButton)

        signupButton.setOnClickListener{
            var email = u_email.text.toString()
            var pass = u_password.text.toString()

            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this,"Please fill in all the fields",Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully registered", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}