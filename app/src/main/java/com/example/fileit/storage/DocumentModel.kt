package com.example.fileit.storage

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


class DocumentModel

 {
//    constructor():this("",null,"","","",null)
     var year: String =""
     var additional_name: String? =null
     var docType: String =""
     var userUid: String =""
     var downloadURL: String =""
      @ServerTimestamp
     var timestamp: Date? = null
     var filename: String = ""

     constructor()



}