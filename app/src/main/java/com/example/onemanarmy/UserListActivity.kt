package com.example.onemanarmy

import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserListActivity : AppCompatActivity(){

    private lateinit var contacts: ArrayList<Contact>
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        // Lookup the recycler view in activity layout
        val rvContacts = findViewById<View>(R.id.rvContacts) as RecyclerView
        // Initialize contacts
        contacts = Contact.createContactsLists(20)
        //Create adapter passing in the simple user data
        val adapter = ContactsAdapter(contacts)
        // Attach the adapter to the recyclerview to populate items
        rvContacts.adapter = adapter
        // Set layout manager to positions the items
        rvContacts.layoutManager = LinearLayoutManager(this)
        // Sheeshhhhhh
    }

}