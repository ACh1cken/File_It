package com.example.fileit.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.example.fileit.MainActivity
import com.example.fileit.R
import com.example.fileit.auth.SignupActivity
import com.example.fileit.storage.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val greyBackground = view.findViewById<RelativeLayout>(R.id.settings_dim_layout)
        val signOutButton = view.findViewById<Button>(R.id.logout_button)
        val deleteAllDataButton = view.findViewById<Button>(R.id.delete_all_data_button)
        val reminderButton = view.findViewById<Button>(R.id.disable_notification)
        val settingsPref = requireActivity().getSharedPreferences("Settings",Context.MODE_PRIVATE)
        val email_display = view.findViewById<TextView>(R.id.setting_display_email)
        val uid_display = view.findViewById<TextView>(R.id.setting_display_uid)

        val helpButton = view.findViewById<ImageButton>(R.id.settings_help)

        helpButton.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setTitle("Help center")
                .setMessage("Manage various account related settings here.")
                .setPositiveButton("Okay"){_,_ ->}
                .show()
        }



        auth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener {}

            if (auth.currentUser == null) {
                val intent = Intent(requireContext(), SignupActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            val userUid = auth.uid
            val userEmail = auth.currentUser!!.email
            email_display.text = userEmail
            uid_display.text = userUid
            Log.e("Settings",userUid+userEmail)


        //shared perference for reminder



        signOutButton.setOnClickListener{
            val alertDialog = AlertDialog
                .Builder(requireContext())
                .setMessage("Do you want to sign out from the current account?")
                .setPositiveButton("Confirm") { dialog, id ->

                    FirebaseAuth.getInstance().signOut()
                    greyBackground.visibility = View.VISIBLE

                    if(FirebaseAuth.getInstance().currentUser == null) {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                        greyBackground.visibility = View.GONE
                        dialog.dismiss()

                    }else{
                        Toast.makeText(requireContext(),"Sign out process failed, please try again. ",Toast.LENGTH_LONG).show()
                        greyBackground.visibility = View.GONE
                        dialog.cancel()
                    }

                }
                .setNegativeButton("Cancel"){
                        dialog , id->
                    greyBackground.visibility = View.GONE
                    dialog.cancel()
                }
                .show()


        }

        reminderButton.setOnClickListener{
            val editor = settingsPref.edit()
            if (settingsPref.getBoolean("NoReminder",true)){
                reminderButton.text = "Enabled"
                reminderButton.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.teal_200))
                editor.putBoolean("NoReminder",false)
                editor.apply()
            }else{
                reminderButton.text = "Disabled"
                reminderButton.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.lightGrey))
                editor.putBoolean("NoReminder",true)
                editor.apply()
                AlertDialog.Builder(requireContext())
                    .setTitle("Notification Scheduled")
                    .setMessage(
                        "Please navigate to the reminder section to fully remove the alert.")
                    .setPositiveButton("Okay"){_,_ ->}
                    .show()
            }

            Toast.makeText(requireContext(),
                "Reminder is set to ${!settingsPref.getBoolean("NoReminder",true)}",
                Toast.LENGTH_SHORT)
                .show()


        }



        deleteAllDataButton.setOnClickListener{

            val alertDialog = AlertDialog
                .Builder(requireContext())
                .setMessage("Delete all files from cloud storage? (Data cannot be recovered)")
                .setPositiveButton("Confirm") { dialog, id ->
                    FirestoreRepository().deleteAccount()
                    dialog.dismiss()

                    startActivity(Intent(requireContext(), MainActivity::class.java))

                }
                .setNegativeButton("Cancel"){
                        dialog , id->
                    dialog.cancel()
                }
                .show()
        }

        return view
    }

}