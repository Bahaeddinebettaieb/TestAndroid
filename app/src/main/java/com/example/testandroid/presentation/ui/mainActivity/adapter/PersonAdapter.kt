package com.example.testandroid.presentation.ui.mainActivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testandroid.R
import com.example.testandroid.data.entites.Person
import kotlinx.android.synthetic.main.item_person.view.*

class PersonAdapter(
    private var list: List<Person>
) : RecyclerView.Adapter<PersonAdapter.UpcomingEventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UpcomingEventViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_person,
            parent,
            false
        )
    )

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: UpcomingEventViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class UpcomingEventViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        init {
            //   itemView.setOnClickListener{}
        }


        fun bind(upcomingEvent: Person) {
            view.apply {
                firstname_txt.text = upcomingEvent.firstname
                lastname_txt.text = upcomingEvent.lastname
                birthday_txt.text = upcomingEvent.birthday


            }


        }
    }

}