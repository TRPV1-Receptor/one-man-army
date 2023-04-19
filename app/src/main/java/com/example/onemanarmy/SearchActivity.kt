package com.example.onemanarmy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.onemanarmy.databinding.ActivitySearchBinding
import java.security.acl.Owner

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var userArrayList: ArrayList<OwnerModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

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