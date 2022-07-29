package com.example.fileit.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fileit.R
import com.example.fileit.database.dbViewModel


class WelcomePageDisplay : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view =  inflater.inflate(R.layout.fragment_welcome_page_display, container, false)

        //TODO("ADD IN DISPLAY FROM DB")

        val usernameDisplay = view.findViewById<TextView>(R.id.username_display)
        usernameDisplay.text  = arguments?.getString("user_name")


//        Handler(Looper.myLooper()!!).postDelayed({
//            findNavController().navigate(R.id.signupActivity)
//        },4000)

        return view
    }


}