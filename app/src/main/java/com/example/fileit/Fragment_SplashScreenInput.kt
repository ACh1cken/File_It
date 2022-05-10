package com.example.fileit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment


/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_SplashScreenInput.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_SplashScreenInput : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash_screen_input,container,false)
        val enterbtn : ImageButton = view.findViewById(R.id.UsernameEnterButton)
        enterbtn.setOnClickListener {
            val username = view.findViewById<EditText>(R.id.edittext).text

            val fragment = SplashScreenUname()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment,fragment)?.commit()
        }
        return view
    }
}