package com.example.fileit.storage

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp


class DocumentModel(
    var year: String ,
    var additional_name: String? ,
    var docType: String ,
    var userUid: String ,
    var downloadURL: String ,
    var timestamp: FieldValue?
) {
    constructor():this("",null,"","","",null)

}