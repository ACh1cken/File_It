package com.example.fileit.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fileit.R
import com.example.fileit.webcrawler.ExtractedData
import java.util.*

class AnnouncementRecyclerAdapter(
//    private val onClickListener :(View,String?)-> Unit
val listener: onClickListener
) : RecyclerView.Adapter<AnnouncementRecyclerAdapter.ViewHolder>() {
    private var extractedData: List<ExtractedData> = emptyList()

    interface onClickListener{
        fun onItemClick(string: String?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementRecyclerAdapter.ViewHolder {
        Log.e("CREATE","LOG: CALLED CREATE")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.announcement_layout_card,parent,false)


        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: AnnouncementRecyclerAdapter.ViewHolder, position: Int) {
//        val index : Int
//        if (position == extractedData.size){
//           index = position
//        }else {
//            index = position + 1
//        }
        val currentItem = extractedData[position]
        Log.e("BIND","LOG: CALLED BIND")
        if (currentItem.announcement.isNullOrBlank()){
            holder.itemView.layoutParams = LinearLayout.LayoutParams(0,0)
            println(currentItem.announcement.isNullOrBlank())
        }else {
            println(currentItem)
            holder.textViewAnnouncement.text = currentItem.announcement
            holder.textViewDate.text = currentItem.announcementDate
            //holder.itemView.setOnClickListener { view -> onClickListener.invoke(view,currentItem.announcementLink) }
        }
    }


    override fun getItemCount(): Int {
        println("LOG.SIZE: " +extractedData.size)
        return extractedData.size
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewAnnouncement : TextView = itemView.findViewById(R.id.announcementTitle)
        val textViewDate : TextView = itemView.findViewById(R.id.announcementDate)
        init {
            itemView.setOnClickListener{
                val currentItem = extractedData[adapterPosition]
            listener.onItemClick(currentItem.announcementLink)
            }
        }

    }

    fun setData(list:List<ExtractedData>){
        this.extractedData = list
        Log.e("Observe","Observer called")
    }

}