package com.example.fileit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton

class SplashScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)

        val inputfragment = Fragment_SplashScreenInput()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,inputfragment)
            commit()
        }

    }

}