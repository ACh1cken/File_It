package com.example.fileit.popupComponents

import android.content.Context
import android.widget.PopupWindow

class Announcement_PopupWindow : PopupWindow() {
    lateinit var ctxt : Context

    fun showPopup(ctxt : Context){
        this.ctxt = ctxt
    }

    fun setPopup(ctxt: Context){

        val popup : PopupWindow = PopupWindow(ctxt)

    }

}