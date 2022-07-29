package com.example.fileit.storage

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fileit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage

class DocumentAdapter(options: FirestoreRecyclerOptions<DocumentModel>, context: Context?):FirestoreRecyclerAdapter<DocumentModel,DocumentAdapter.DocumentViewHolder>(options) {
    val mContext = context

    private val db = FirebaseFirestore.getInstance()
    val userUid = FirebaseAuth.getInstance().uid
    private val docRef = db.collection("users").document(userUid!!).collection("documents")
    val greyBackground = (mContext as Activity).window.decorView.findViewById<RelativeLayout>(R.id.createNewEntry_bac_dim_layout)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        Log.e("onCreateViewHolder","Viewholdercreation")
        return DocumentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.new_entry_cardview,parent,false))
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int, model: DocumentModel) {
        holder.filename.text = model.filename
        holder.fileyear.text = model.year
        Log.e("Query","${model.filename}")
        //addmorefields
        holder.itemView.setOnClickListener{
//            greyBackground.visibility = View.VISIBLE
            showPopup(it,model)
        }

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

    private fun showPopup(view: View, model: DocumentModel){
        val popup_view = LayoutInflater.from(mContext).inflate(R.layout.popup_layout,null);
        val popupWindow  = PopupWindow(popup_view,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,true)
        popupWindow.showAtLocation(view,Gravity.CENTER,0,0)
        val dismissButton = popup_view.findViewById<ImageButton>(R.id.close_popup_button)
        val deleteButton = popup_view.findViewById<ImageButton>(R.id.popup_delete_button)

        val filename = popup_view.findViewById<TextView>(R.id.popup_filename_textview)
        val filetype = popup_view.findViewById<TextView>(R.id.popup_filetype)
        val file_description = popup_view.findViewById<TextView>(R.id.popup_additional_details)
        val fileYear = popup_view.findViewById<TextView>(R.id.popup_year)
        val fileAmount = popup_view.findViewById<TextView>(R.id.popup_amount)

        filename.append(model.filename)
        filetype.append(model.docType)
        if (model.additional_name!=null) {
            file_description.append(model.additional_name)
            file_description.visibility = View.VISIBLE
        }
        fileYear.append(model.year)
        if(model.fileAmount != 0){
            fileAmount.append(model.fileAmount.toString())
            fileAmount.visibility = View.VISIBLE
        }


        dismissButton.setOnClickListener(){
            Log.e("Popup","Dismissed")
            popupWindow.dismiss()
        }

        popupWindow.setOnDismissListener {
//            greyBackground.visibility = View.GONE
        }


        deleteButton.setOnClickListener{
            Log.e("DeleteButton","Test")

            //dim background

             AlertDialog
                .Builder(mContext)
                .setMessage("Delete the file permanently from cloud? (Cannot be recovered)")
                .setPositiveButton("Confirm") { dialog, id ->
                    deleteEntry(model.filename,model.docType,model.year)
                    dialog.dismiss()
                    popupWindow.dismiss()
                }
                .setNegativeButton("Cancel"){
                    dialog , id->
                    dialog.cancel()
                }
                .show()
        }

    }
    private fun deleteEntry(filename: String, docType: String, year: String, ) {
//Creat
        Log.e("Delete Entry","$filename, $docType, $year")
        var query = docRef
            .whereEqualTo("filename",filename)
            .whereEqualTo("docType",docType)
            .whereEqualTo("year",year)
            //.whereEqualTo("year",year)
            .get()
            .addOnSuccessListener {
                documents ->
                for(document in documents){
                    Log.e("delete document","${document.data}")
                    val storage = FirebaseStorage.getInstance()
                    val deleteRef = storage.getReferenceFromUrl(document.data["downloadURL"] as String)
                    deleteRef.delete().addOnSuccessListener {
                        Log.e("Delete","File deleted at ${document.data["downloadURL"]}")
                        document.reference.delete()
                    }.addOnFailureListener{
                        Log.e("Delete","${document.data["downloadURL"]} not deleted")
                    }
                }

            }
            .addOnFailureListener{
                Toast.makeText(mContext,"Document does not exist",Toast.LENGTH_SHORT).show()
                Log.e("Delete",it.message.toString())
            }


    }
}