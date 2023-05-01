package com.example.onemanarmy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import com.example.onemanarmy.databinding.ActivitySearchBinding
import com.google.android.material.search.SearchBar
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.security.acl.Owner
import java.util.*
import java.util.Locale.filter
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var userArrayList: ArrayList<OwnerModel>
    private lateinit var allOwners: ArrayList<Map<String,String>>
    private var filtered = ArrayList<OwnerModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allOwners = ArrayList()
        userArrayList = ArrayList()


        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users")

        val name = ArrayList<String>()
        val service = ArrayList<String>()
        val busPhone = ArrayList<String>()
        val busName = ArrayList<String>()
        val email = ArrayList<String>()
        val address = ArrayList<String>()
        val profilePic = ArrayList<String>()


        //Pulling all accounts from database that are owners
        usersRef.orderByChild("userType").equalTo("owner")
            .addListenerForSingleValueEvent(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for(child in snapshot.children){
                            allOwners.add(child.value as Map<String, String>)
                        }
                    }
                    for(owner in allOwners){
                        Log.d("Owner", owner.toString())
                        name.add(owner["firstName"].toString())
                        service.add(owner["serviceProvided"].toString())
                        busPhone.add(owner["businessPhone"].toString())
                        busName.add(owner["businessName"].toString())
                        email.add(owner["businessEmail"].toString())
                        address.add(owner["businessAddress"].toString())
                        owner["profilePicture"]?.let { profilePic.add(it) }
                    }
                    for(i in name.indices){
                        val user = OwnerModel()
                        Log.d("For Loop", name[i])
                        user.firstName = name[i]
                        user.serviceProvided = service[i]
                        user.profilePicture = profilePic[i]
                        userArrayList.add(user)
                        //TODO profile picture bug, when user does not have it set default
                    }

                }
                override fun onCancelled(error: DatabaseError) {}
            })

        val adapter = OwnerAdapter(this,userArrayList)

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = ArrayList<OwnerModel>()
                for (owner in allOwners){
                    if (owner["firstName"].toString().lowercase().contains(newText?.lowercase().toString()) || owner["serviceProvided"].toString().lowercase()
                            .contains(newText?.lowercase().toString())
                    ){
                        filteredList.add(OwnerModel(
                            firstName = owner["firstName"],
                            serviceProvided = owner["serviceProvided"],
                            profilePicture = owner["profilePicture"]))
                    }
                }
                adapter.update(filteredList)
                binding.searchListView.adapter=adapter
                return false
            }

        })

        binding.searchListView.adapter = adapter
        binding.searchListView.isClickable = true

        binding.searchListView.setOnItemClickListener { adapterView, view, position, item ->

            Log.d("item",item.toString())
            Log.d("position",position.toString())


            val userClicked = adapterView.getItemAtPosition(position) as OwnerModel

            val user = OwnerModel()

            for(owner in allOwners){
                if (userClicked.firstName?.lowercase().toString() == owner["firstName"]?.lowercase()){
                    user.firstName = owner["firstName"]
                    user.businessPhone = owner["businessPhone"]
                    user.businessName = owner["businessName"]
                    user.serviceProvided = owner["serviceProvided"]
                    user.businessEmail = owner["businessEmail"]
                    user.businessAddress = owner["businessAddress"]
                    user.profilePicture = owner["profilePicture"]
                    user.businessBio = owner["Biography"]

                }
            }

            val intent = Intent(this,AccountActivity::class.java)

            intent.putExtra("name", user.firstName.toString())
            intent.putExtra("phone",user.businessPhone.toString())
            intent.putExtra("businessName", user.businessName.toString())
            intent.putExtra("service",user.serviceProvided.toString())
            intent.putExtra("email",user.businessEmail.toString())
            intent.putExtra("address",user.businessAddress.toString())
            intent.putExtra("profile",user.profilePicture.toString())
            intent.putExtra("bio",user.businessBio.toString())

            startActivity(intent)
        }

    }


}