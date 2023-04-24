package com.example.onemanarmy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.onemanarmy.databinding.ActivitySearchBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.security.acl.Owner

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var userArrayList: ArrayList<OwnerModel>
    private lateinit var allOwners: ArrayList<Map<String,String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users")


        //Pulling all accounts from database that are owners
        usersRef.orderByChild("userType").equalTo("owner")
            .addListenerForSingleValueEvent(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for(child in snapshot.children){
                            allOwners.add(child.value as Map<String, String>)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        allOwners = ArrayList()

        val button = findViewById<Button>(R.id.test)

        button.setOnClickListener {

            for(owner in allOwners){
                Log.d("Owner",owner.toString())
            }
        }



        val imageId = intArrayOf(
            R.drawable.elias,
            R.drawable.austin,
            R.drawable.michael,
        )

        val name = arrayOf(
            "Elias",
            "Austin",
            "Michael",
        )

        val service = arrayOf(
            "Auror",
            "Potions",
            "Wand Making"
        )

        val busPhone = arrayOf(
            "252-333-4567",
            "252-123-4321",
            "252-765-5678",
        )

        val busName = arrayOf(
            "Elias Business",
            "Austin Business",
            "Michael Business",
        )

        val email = arrayOf(
            "Elias@onemanarmy.com",
            "Austing@onemanarmy.com",
            "Michael@onemanarmy.com",
        )

        val address = arrayOf(
            "123 Fairy Lane",
            "999 Spoon Drive",
            "456 Larry Court",
        )

        userArrayList = ArrayList()

        for(i in name.indices){
            val user = OwnerModel()
            user.firstName = name[i]
            user.serviceProvided = service[i]
            user.businessPhone = busPhone[i]
            user.businessName = busName[i]
            user.businessEmail = email[i]
            user.businessAddress = address[i]
            user.profilePic = imageId[i]

            userArrayList.add(user)
        }

        binding.searchListView.isClickable = true
        binding.searchListView.adapter = OwnerAdapter(this, userArrayList)
        binding.searchListView.setOnItemClickListener { _, _, position, _ ->

            val name = name[position]
            val phone = busPhone[position]
            val busName = busName[position]
            val image = imageId[position]
            val service = service[position]
            val email = email[position]
            val address = address[position]

            val intent = Intent(this,AccountActivity::class.java)

            intent.putExtra("name", name)
            intent.putExtra("phone",phone)
            intent.putExtra("businessName", busName)
            intent.putExtra("profilePic",image)
            intent.putExtra("service",service)
            intent.putExtra("email",email)
            intent.putExtra("address",address)

            startActivity(intent)
        }
    }
}