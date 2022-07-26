package com.example.fileit.storage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class FirestoreViewModel (
    var mFirestore :FirebaseFirestore = FirebaseFirestore.getInstance(),
    var firestoreRepository :FirestoreRepository = FirestoreRepository(),
) : ViewModel() {
    private lateinit var expandableListTitle: MutableLiveData<ArrayList<String>>
    private lateinit var expandableListDetail: MutableLiveData<HashMap<String,MutableList<DocumentModel>>>
    var mutableFirestoreData : MutableLiveData<List<DocumentModel>> =  MutableLiveData(emptyList())

    init {
        getData()
    }

    private fun getData(){
        viewModelScope.launch {
            mutableFirestoreData  = firestoreRepository.onPostValueChanged()
            expandableListDetail = firestoreRepository.getExpandableListDetail()
            expandableListTitle = firestoreRepository.getExpandableListTitle()
        }
    }



     fun getLiveData(): MutableLiveData<List<DocumentModel>> {
        return mutableFirestoreData
    }

     fun getListTitle(): MutableLiveData<ArrayList<String>> {
        return expandableListTitle
    }
    fun getListDetail(): MutableLiveData<HashMap<String, MutableList<DocumentModel>>> {
        return expandableListDetail
    }

    fun stopListener(){
        firestoreRepository.stopListener()
    }


}