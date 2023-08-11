package com.example.testandroid.presentation.ui.mainActivity

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testandroid.data.entites.Person
import com.example.testandroid.presentation.base.BaseViewModel

class MainViewModel : BaseViewModel<MainNavigator>() {
    private val TAG = "MainViewModel"

    var firstname = ObservableField<String>()
    var lastname = ObservableField<String>()
    var birthday = ObservableField<String>()
//    var personList = ObservableArrayList<Person>()

    val personList: MutableLiveData<List<Person>> by lazy {
        MutableLiveData<List<Person>>()
    }

    private var personRepository = PersonRepository()


    fun insertPerson(person: Person){
        personRepository.insertPerson(person)
    }

    fun getAllPersons() {
        Log.e(TAG, "getAllPersons: "+personRepository.getAllPersons())
        personList.postValue(personRepository.getAllPersons().value)
        Log.e(TAG, "personList: ${personList.value}")
    }
}