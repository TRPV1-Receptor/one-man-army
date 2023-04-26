package com.example.onemanarmy

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.onemanarmy.databinding.ActivityAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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


        if (intent.hasExtra("user")) {
            val userIntent = intent.extras
            currentUser = userIntent?.getSerializable("user") as OwnerModel

            var profilePic = currentUser.profilePicture

            val imgReference = profilePic?.let { Firebase.storage.getReferenceFromUrl(it.toString()) }

            val ONE_MEGABYTE: Long = 1024 * 1024

            if (profilePic == null) {
                profilePic = R.drawable.profile_icon.toString()
            }

            binding.profileName.text = currentUser.firstName
            binding.profilePhone.text = currentUser.businessPhone
            binding.profileBusinessName.text = currentUser.businessName

            if (imgReference != null) {
                imgReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    binding.profilePicture.setImageBitmap(bmp)
                }.addOnFailureListener {
                    // handle failure case
                }
            } else {
                binding.profilePicture.setImageResource(R.drawable.profile_icon)
            }

            binding.serviceOffered.text = currentUser.serviceProvided
            binding.profileEmail.text = currentUser.businessEmail
            binding.profileAddress.text = currentUser.businessAddress
        }else{
            //grabbing the Information from the intent
            val name = intent.getStringExtra("name")
            val phone= intent.getStringExtra("phone")
            val busName = intent.getStringExtra("businessName")


            val profilePic = intent.getIntExtra("profilePicture",R.drawable.profile_icon) // The profile icon is the default if they have no pic


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