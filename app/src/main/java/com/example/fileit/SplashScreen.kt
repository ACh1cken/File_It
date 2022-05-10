package com.example.fileit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
          val intent = Intent(this@SplashScreen, SplashScreen2::class.java)
            startActivity(intent)
            finish()//prevents the logo from showing again if back button is pushed
        },3000)
    }
}