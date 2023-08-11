package com.example.testandroid.domain.localDate.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testandroid.data.entites.Person
import com.example.testandroid.domain.localDate.dao.PersonDao


@Database(entities = [Person::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    private val TAG = "AppDatabase"

    abstract fun personDao(): PersonDao
}

private lateinit var INSTANCE: AppDatabase
private val TAG = "AppDatabase"
fun getDatabase(context: Context): AppDatabase {

    synchronized(AppDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            Log.e(TAG, "getDatabase: ")
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "testAndroid.db"
            ).build()
        }
    }
    return INSTANCE

}