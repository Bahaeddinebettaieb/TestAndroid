package com.example.testandroid.data.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Person(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var birthday: String? = null,
):Serializable{
    override fun toString(): String {
        return "Person(firstname=$firstname, lastname=$lastname, birthday=$birthday)"
    }
}