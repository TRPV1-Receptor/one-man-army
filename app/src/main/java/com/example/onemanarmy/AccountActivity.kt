package com.example.onemanarmy

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private val ONE_MEGABYTE: Long = 1024 * 1024

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

            val imgReference = profilePic?.let { Firebase.storage.getReferenceFromUrl(it) }

            if (imgReference != null) {
                imgReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    binding.profilePicture.setImageBitmap(bmp)
                }.addOnFailureListener {
                    Log.d("Exception", it.toString())
                }
            } else {
                binding.profilePicture.setImageResource(R.drawable.profile_icon)
            }

            binding.profileName.text = currentUser.firstName
            binding.profilePhone.text = currentUser.businessPhone
            binding.profileBusinessName.text = currentUser.businessName
            binding.serviceOffered.text = currentUser.serviceProvided
            binding.profileEmail.text = currentUser.businessEmail
            binding.profileAddress.text = currentUser.businessAddress
            binding.Bio.text = currentUser.businessBio
        }else{
            //grabbing the Information from the intent
            val name = intent.getStringExtra("name")
            val phone= intent.getStringExtra("phone")
            val busName = intent.getStringExtra("businessName")
            val profilePic = intent.getStringExtra("profile") // The profile icon is the default if they have no pic
            val service = intent.getStringExtra("service")
            val email = intent.getStringExtra("email")
            val address = intent.getStringExtra("address")
            val bio = intent.getStringExtra("bio")

            var imgreference = profilePic?.let { Firebase.storage.getReferenceFromUrl(it) }

            if (imgreference!=null){
                imgreference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes->
                    val bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                    binding.profilePicture.setImageBitmap(bmp)
                }.addOnFailureListener{
                    Log.d("Exeption",it.toString())
                }
            }else{
                binding.profilePicture.setImageResource(R.drawable.profile_icon)
            }

            //binding them to the view
            binding.profileName.text = name
            binding.profilePhone.text = phone
            binding.profileBusinessName.text = busName
            binding.serviceOffered.text = service
            binding.profileEmail.text = email
            binding.profileAddress.text = address
            binding.Bio.text= bio
        }




        val backBtn = findViewById<ImageView>(R.id.back)
        backBtn.setOnClickListener { finish() }
    }
}