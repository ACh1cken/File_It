package com.example.fileit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fileit.R
import com.example.fileit.webcrawler.extractedData
import com.example.fileit.webcrawler.webcrawler
import kotlinx.android.synthetic.main.fragment_announcement.*


class AnnouncementFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_announcement, container, false)
        val model : webcrawler by activityViewModels()
        val extractedListData : List<extractedData> = model.getData()



        announcementRecyclerView.adapter = AnnouncementRecyclerAdapter(extractedListData)
        announcementRecyclerView.layoutManager = LinearLayoutManager(view.context)

        return view
    }


}