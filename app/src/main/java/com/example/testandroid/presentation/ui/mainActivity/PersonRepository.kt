package com.example.testandroid.presentation.ui.mainActivity

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.testandroid.App
import com.example.testandroid.data.entites.Person
import com.example.testandroid.domain.localDate.database.getDatabase

class PersonRepository {
    private val TAG = "PersonRepository"

    fun insertPerson(person: Person) {
        Log.e(TAG, "insertPerson: $person")
        Thread(Runnable {
            getDatabase(App.context).personDao().insertPerson(person)
            getAllPersons()
        }).start()
    }

    fun getAllPersons(): LiveData<List<Person>> {
        Log.e(TAG, "getAllPersons: ")
        return getDatabase(App.context).personDao().getAllPersons()
    }
}