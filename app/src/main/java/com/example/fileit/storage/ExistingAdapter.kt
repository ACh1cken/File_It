package com.example.fileit.storage

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.graphics.Typeface
import android.net.Uri
import android.os.Environment
//import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.fileit.R
import com.google.firebase.storage.FirebaseStorage


class ExpandableAdapter(
    context: Context,
    view: View

//    expandableListTitle: ArrayList<String>,
//    expandableListDetail: HashMap<String,MutableList<DocumentModel>>
    ): BaseExpandableListAdapter() {

    val mContext = context
    var headerItem = ArrayList<String>()
    var childItem = HashMap<String,MutableList<DocumentModel>>()
    val storageRef = FirebaseStorage.getInstance()
    val isDownloading : Boolean = false

    val dm = mContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//    val downloadProgressBar = (mContext as Activity).window.decorView.findViewById<ProgressBar>(R.id.view_existing_download_bar)
//    val downloadFilename  = (mContext as Activity).window.decorView.findViewById<TextView>(R.id.view_existing_download_filename)
//    val greyBackground = (mContext as Activity).window.decorView.findViewById<RelativeLayout>(R.id.existing_bac_dim_layout)
    val greyBackground = view.findViewById<RelativeLayout>(R.id.existing_bac_dim_layout)
//    var inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    fun setTitleData(header:ArrayList<String>){
        this.headerItem =header
//        Log.e("Observer","TitleListObserver")
    }
    fun setDetailData(child:HashMap<String,MutableList<DocumentModel>>){
        this.childItem = child
//        Log.e("Observer","DetailListObserver")
    }

    override fun getChild(headerPos: Int, childPos: Int): DocumentModel {
//        return childItem.get(headerItem[headerPos])
        return childItem[headerItem[headerPos]]!![childPos]
    }

    override fun getChildId(headerPos: Int, childPos: Int): Long {
        return childPos.toLong()
    }


    override fun getChildView(
        headerPos: Int, childPos: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup?
    ): View? {
        var convertView = convertView


        val doc: DocumentModel = getChild(headerPos, childPos)
        val expandedListText = doc.filename
        val downloadUrl = doc.downloadURL


        if (convertView == null) {
//            val layoutInflater = this.mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            convertView = layoutInflater.inflate(R.layout.view_existing_cardview, null)

            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_existing_cardview,null)

        }

        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.expandedListItem)

        expandedListTextView.text = expandedListText
        convertView.setOnClickListener{
            //Grey Background
            greyBackground.visibility = View.VISIBLE



            val alertDialog = AlertDialog
                .Builder(mContext)
                .setMessage("Download file?")
                .setPositiveButton("Confirm") { dialog, id ->
                    createDownloadRequest(downloadUrl,expandedListText)
                    greyBackground.visibility = View.GONE
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel"){
                        dialog , id->
                    dialog.cancel()
                    greyBackground.visibility = View.GONE
                }
                .show()

        }

        return convertView
    }

    override fun getChildrenCount(headerPos: Int): Int {
        return childItem[this.headerItem[headerPos]]!!.size
    }

    override fun getGroupCount(): Int {
        return headerItem.size
    }


    override fun getGroup(headerPos: Int): String? {
        return headerItem.get(headerPos)
    }


    override fun getGroupId(headerPos: Int): Long {
        return headerPos.toLong()
    }


    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View? {

        var convertView = convertView

        if (convertView == null) {
//            val layoutInflater =
//                this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            convertView = layoutInflater.inflate(R.layout.view_existing_listgroup, null)
//
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_existing_listgroup,null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listTitle)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = getGroup(listPosition)


        return convertView
    }



    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }


    override fun hasStableIds(): Boolean {
        return false
    }

    private fun createDownloadRequest(downloadUrl: String, filename : String){
//        textView.append(" $filename")
//        textView.visibility = View.VISIBLE
//        progressbar.visibility = View.VISIBLE

        Toast.makeText(mContext,"Downloading file: $filename",Toast.LENGTH_SHORT).show()
        val request =
            DownloadManager.Request(Uri.parse(downloadUrl))
                .setTitle(filename)
                .setDescription("Downloading file...")
                .setDestinationInExternalFilesDir(mContext,Environment.DIRECTORY_DOWNLOADS,filename)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadID = dm.enqueue(request)


    }

}