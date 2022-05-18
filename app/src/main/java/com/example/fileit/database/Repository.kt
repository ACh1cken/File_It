package com.example.fileit.database

class Repository(private val userDao: userDao) {
    suspend fun addUser(user : User){
        userDao.addUser(user)
    }

}