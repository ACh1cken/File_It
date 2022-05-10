package com.example.fileit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [

    User::class,
    Year::class,
    EAForm::class,
    Child::class,
    Insurance::class,
    Lifestyle::class,
    Medical::class,
    Others::class
                     ],
    version = 1,
    exportSchema = true)
abstract class Fileit_Database : RoomDatabase(){

    abstract fun userDao() : userDao

    companion object{
    @Volatile
    private var INSTANCE : Fileit_Database? = null

        fun getDatabase(context : Context) : Fileit_Database{
            val tmpInstance = INSTANCE
            if(tmpInstance != null){
                return tmpInstance
            }

        //singleton database
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Fileit_Database::class.java,
                    "FileItDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }


    }
}
