package com.example.fileit.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.fileit.MainPage
import com.example.fileit.R
import com.gargoylesoftware.htmlunit.android.Main
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var u_email : EditText
    private lateinit var u_password : EditText
    private lateinit var loginButton : Button
    private lateinit var registerAnchor : TextView
    private lateinit var forgotPasswordAnchor : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        u_email = findViewById(R.id.login_email_ed)
        u_password = findViewById(R.id.login_pass_ed)

        loginButton = findViewById(R.id.loginButton)
        registerAnchor = findViewById(R.id.registerAnchor)
        forgotPasswordAnchor = findViewById(R.id.forgotPassAnchor)

        if (auth.currentUser != null){
            startActivity(Intent(this,MainPage::class.java))
            finish()

        }

        registerAnchor.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        forgotPasswordAnchor.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
            finish()
        }
        loginButton.setOnClickListener{
            var email = u_email.text.toString()
            var pass = u_password.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                Toast.makeText(this,"Please fill in all the fields", Toast.LENGTH_LONG).show()
            }else{
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainPage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}