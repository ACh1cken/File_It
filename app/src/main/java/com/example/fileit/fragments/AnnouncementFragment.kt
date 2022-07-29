package com.example.fileit.fragments

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fileit.R
import com.example.fileit.webcrawler.webcrawler


class AnnouncementFragment : Fragment() , AnnouncementRecyclerAdapter.onClickListener{
    private val model : webcrawler by activityViewModels()
    private lateinit var pref :SharedPreferences
    private lateinit var relativeLayout: ConstraintLayout



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(model._initCount == 0) {
            model.updateData()
            model.updateInit()
//            Log.e("init",model._initCount.toString())
        }



        val recyclerview = view.findViewById<RecyclerView>(R.id.announcementRecyclerView)

        val announcementadapter = AnnouncementRecyclerAdapter(this)
        recyclerview.adapter = announcementadapter
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.setHasFixedSize(false)


        model.extractedData.observe(viewLifecycleOwner) { list ->
            announcementadapter.setData(list)
            
            announcementadapter.notifyDataSetChanged()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_announcement, container, false)

        val helpButton = view.findViewById<ImageButton>(R.id.announcement_help)

        helpButton.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setTitle("Help center")
                .setMessage("Announcement are directly fetched from Lembaga Hasil Dalam Negeri news website. \n" +
                        "Press on individual item to view them in detail. ")
                .setPositiveButton("Okay"){_,_ ->}
                .show()
        }

        relativeLayout=view.findViewById(R.id.reminderAnnouncement)
        val reminderAnnouncementDate = view.findViewById<TextView>(R.id.reminderAnnouncement_date)
        pref = requireContext().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        if(pref.getBoolean("NoReminder",true)){
            relativeLayout.visibility = View.GONE
        }else{
            relativeLayout.visibility = View.VISIBLE
            reminderAnnouncementDate.text =
                "${pref.getLong("ReminderDay", 1)}/" +
                    "${pref.getLong("ReminderMonth", 1)}/" +
                    "${pref.getLong("ReminderYear", 1990)}"
        }


            return view
    }

    override fun onItemClick(string: String?) {
        //showToast("Test"+ position)
//        Log.e("Click", " $string")
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(string)))
    }



    

}