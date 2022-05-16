package com.example.fileit.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class dbViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : Repository

    init {
        val userDao = Fileit_Database.getDatabase(application).userDao()
        repository = Repository(userDao)
    }

    fun addUser(user : User){
        viewModelScope.launch (Dispatchers.IO){
            repository.addUser(user)
        }
    }

    fun getUsername(id : Int){}

}