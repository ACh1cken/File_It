package com.example.fileit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface userDao {
//queries

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Query("Select user_name from user_table where UID = :id")
    fun getUsername(id: Int): String

    @Query("Select * from user_table order by UID ASC")
    fun readAllData(): Flow<List<User>>

    @Query("Select * from year order by Year_ID ASC")
    fun readAllYearData(): Flow<List<Year>>

    @Query("Select * from child where child.Year_ID = :year order by C_ID ASC")
    fun readAllChildData(year: Int): Flow<List<Child>>
}