package com.example.fileit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fileit.R
import com.example.fileit.database.User
import com.example.fileit.database.dbViewModel


class WelcomePageInput : Fragment() {
    private lateinit var mUserViewModel : dbViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome_page_input, container, false)
        val enterbtn : ImageButton = view.findViewById(R.id.imageButton)

        mUserViewModel = ViewModelProvider(this).get(dbViewModel::class.java)

        enterbtn.setOnClickListener {
            var username = view.findViewById<EditText>(R.id.usernameinput).text.toString()
            val bundle : Bundle

            insertUser(username)
            if (username.isNotEmpty()) {
                bundle = bundleOf(
                    Pair("user_name",username)
                )
            }else{
                bundle = bundleOf(
                    Pair("user_name","User")
                )
            }

//            findNavController().navigate(R.id.welcomePageDisplay,bundle)

        }
        return view
    }
        private fun insertUser(username : String){
            if (username.isNotEmpty()){
                var user = User(0,username)
                mUserViewModel.addUser(user)
            }else{
                //default username
                var username = "User"
                val user = User(0,username)
                mUserViewModel.addUser(user)
            }
        }
}