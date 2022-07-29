package com.example.fileit.storage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage

class FirestoreRepository {
    private var _firestoreLiveData: MutableLiveData<List<DocumentModel>> = MutableLiveData(emptyList())
    private val userUid : String? = FirebaseAuth.getInstance().uid
    private val mFirebase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var storageRefListener : ListenerRegistration
    private lateinit var storageReference: CollectionReference
    private var docMap = MutableLiveData<HashMap<String,MutableList<DocumentModel>>>()
    private var yearArr = MutableLiveData<ArrayList<String>>()
    var firestoreLiveData: LiveData<List<DocumentModel>> = _firestoreLiveData


    fun onPostValueChanged():MutableLiveData<List<DocumentModel>>{
        listenForPostValueChange()
        return _firestoreLiveData
    }

    private fun listenForPostValueChange() {
        storageRefListener = mFirebase.collection("users").document(userUid!!).collection("documents")
            .addSnapshotListener(EventListener<QuerySnapshot>{
                value , error ->
                if (error != null || value == null){
                    return@EventListener
                }
                if (value.isEmpty) {
                    _firestoreLiveData.postValue(emptyList())
                } else {
                    val documents = ArrayList<DocumentModel>()
                    var map = hashMapOf<String,MutableList<DocumentModel>>()

                    var availalbleYear  = ArrayList<String>()
                    for (doc in value) {
                        val document = doc.toObject(DocumentModel::class.java)
                        documents.add(document)
                        if(!availalbleYear.contains(document.year)){

                            availalbleYear.add(document.year)
                            map.put(document.year, mutableListOf())
                        }
                        map.get(document.year)?.add(document)

                    }
                    _firestoreLiveData.postValue(documents)
                    docMap.postValue(map)
                    yearArr.postValue(availalbleYear)
                    firestoreLiveData = _firestoreLiveData
                }
            })



    }

    fun getExpandableListDetail(): MutableLiveData<HashMap<String, MutableList<DocumentModel>>> {
        return docMap
    }
    fun getExpandableListTitle(): MutableLiveData<ArrayList<String>> {
        return yearArr
    }


    fun stopListener(){
        storageRefListener.remove()
    }
    fun deleteAccount() {
        FirebaseFirestore.getInstance()
            .collection("users").document(userUid!!)
            .collection("documents")
            .get()
            .addOnSuccessListener {
                    documents ->
                for (document in documents) {
                    Log.e("delete all document", "${document.data}")

                    val storage = FirebaseStorage.getInstance()

                    val deleteRef = storage.getReferenceFromUrl(document.data["downloadURL"] as String)


                    deleteRef.delete().addOnCompleteListener {
                        Log.e("Delete", "File deleted at ${document.data["downloadURL"]}")

                    }.addOnFailureListener {
                        Log.e("Delete", "${document.data["downloadURL"]} not deleted")
                    }

                    document.reference.delete().addOnFailureListener{
                        Log.e("Delete", "${document.data} not deleted")
                    }.addOnCompleteListener {
                        Log.e("Delete","${document.data} is deleted")
                    }

                }
                FirebaseFirestore.getInstance()
                    .collection("users").document(userUid)
                    .get()
                    .addOnSuccessListener{
                        it.reference.delete()
                        FirebaseAuth.getInstance().currentUser!!.delete()

                    }


            }

    }

}