package com.example.fileit.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fileit.R
import com.example.fileit.webcrawler.extractedData
import org.w3c.dom.Text

class AnnouncementRecyclerAdapter(private val ExtractedData: List<extractedData>): RecyclerView.Adapter<AnnouncementRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementRecyclerAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.announcement_layout_card,parent,false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: AnnouncementRecyclerAdapter.ViewHolder, position: Int) {
        val currentItem = ExtractedData[position]

        holder.textViewAnnouncement.setText(currentItem.announcement)
        holder.textViewDate.setText("Posted on "+currentItem.announcementDate)
    }

    override fun getItemCount(): Int {
        return ExtractedData.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewAnnouncement : TextView = itemView.findViewById(R.id.announcementTitle)
        val textViewDate : TextView = itemView.findViewById(R.id.announcementDate)
    }
}