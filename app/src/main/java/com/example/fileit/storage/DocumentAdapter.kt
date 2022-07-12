package com.example.fileit.storage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fileit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.fragment_create_new_entry.view.*
import kotlinx.android.synthetic.main.new_entry_cardview.view.*

class DocumentAdapter(options: FirestoreRecyclerOptions<DocumentModel>):FirestoreRecyclerAdapter<DocumentModel,DocumentAdapter.DocumentViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        Log.e("onCreateViewHolder","Viewholdercreation")
        return DocumentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.new_entry_cardview,parent,false))
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int, model: DocumentModel) {
        holder.filename.text = model.additional_name
        holder.fileyear.text = model.year
        Log.e("Query","${model.additional_name}")
        //addmorefields
    }

    override fun onError(e: FirebaseFirestoreException) {
        Log.e("FirestoreRecycler",e.message!!)
        super.onError(e)
    }

    class DocumentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var filename = itemView.findViewById<TextView>(R.id.new_entry_filename_textview)
        var fileyear = itemView.findViewById<TextView>(R.id.new_entry_fileYear_textview)
        //addmorefields
    }
}