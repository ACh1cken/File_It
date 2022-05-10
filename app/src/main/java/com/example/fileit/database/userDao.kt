package com.example.fileit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface userDao {
//queries
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user : User)

}