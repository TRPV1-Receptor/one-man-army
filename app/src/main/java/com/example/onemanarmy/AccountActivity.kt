package com.example.onemanarmy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.onemanarmy.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {

    private lateinit var saveData: SaveData
    private lateinit var binding:ActivityAccountBinding

    private lateinit var currentUser : OwnerModel

    override fun onCreate(savedInstanceState: Bundle?) {
        //share preference
        saveData = SaveData(this)
        if (saveData.loadDarkState() == true) {
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        //setting up the page to be dynamically filled in
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if(intent.hasExtra("user")){
            val userIntent = intent.extras
            currentUser = userIntent?.getSerializable("user") as OwnerModel

             /**
             *Code for binding the account page with currently logged in user goes here
             */


        }else{
            //grabbng the Information from the intent
            val name = intent.getStringExtra("name")
            val phone= intent.getStringExtra("phone")
            val busName = intent.getStringExtra("businessName")
            val profilePic = intent.getIntExtra("profilePic",R.drawable.profile_icon) // The profile icon is the deault if they have no pic
            val service = intent.getStringExtra("service")
            val email = intent.getStringExtra("email")
            val address = intent.getStringExtra("address")

            //binding them to the view
            binding.profileName.text = name
            binding.profilePhone.text = phone
            binding.profileBusinessName.text = busName
            binding.profilePicture.setImageResource(profilePic)
            binding.serviceOffered.text = service
            binding.profileEmail.text = email
            binding.profileAddress.text = address
        }




        val backBtn = findViewById<ImageView>(R.id.back)
        backBtn.setOnClickListener { finish() }
    }
}