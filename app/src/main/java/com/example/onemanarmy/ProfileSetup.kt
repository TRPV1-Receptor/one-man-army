package com.example.onemanarmy

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileNotFoundException
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class ProfileSetupActivity : AppCompatActivity(), OnItemClickListener{

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_IMAGE_UPLOAD = 2
        const val REQUEST_PERMISSIONS = 3
    }

    private lateinit var profilePictureImageView: ImageView
    private lateinit var uploadPictureButton: Button
    private lateinit var takePictureButton: Button
    private lateinit var bioEditText: EditText
    private lateinit var skillsSpinner: Spinner
    private lateinit var addSkillButton: Button
    private lateinit var selectedSkillsListView: ListView
    private lateinit var saveButton: Button
    //for use of grabbing unique ID
    //private lateinit var userName: String
    //private lateinit var userID: String

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private lateinit var skillsAdapter: ArrayAdapter<String>
    private var selectedSkillsList= arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setup)


        // Find views
        profilePictureImageView = findViewById(R.id.profile_picture_image_view)
        uploadPictureButton = findViewById(R.id.upload_picture_button)
        takePictureButton = findViewById(R.id.take_picture_button)
        bioEditText = findViewById(R.id.bio_edit_text)
        skillsSpinner = findViewById(R.id.skills_spinner)
        addSkillButton = findViewById(R.id.add_skill_button)
        selectedSkillsListView = findViewById(R.id.skillsListView)
        saveButton = findViewById(R.id.save_button)

        // Set up skills spinner
        val skills = resources.getStringArray(R.array.skills_array)
        skillsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, skills)
        skillsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        skillsSpinner.adapter = skillsAdapter

        val button = findViewById<TextView>(R.id.profilePictureTextView)
        button.setOnClickListener{
            val userID = FirebaseAuth.getInstance().currentUser?.uid
            Toast.makeText(this,userID,Toast.LENGTH_SHORT).show()
        }



        val skillViewAdapter:ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,selectedSkillsList)
        selectedSkillsListView.adapter = skillViewAdapter

        // Set up add skill button
        addSkillButton.setOnClickListener {
            val selectedSkill = skillsSpinner.selectedItem.toString()
            if (selectedSkillsList.contains(selectedSkill)) {
                Toast.makeText(this,"Skill already added!",Toast.LENGTH_SHORT).show()
            }else{
                selectedSkillsList.add(selectedSkill)
                skillViewAdapter.notifyDataSetChanged()
            }
        }

        // Set up selected skills list view


        // Set up upload picture button
        uploadPictureButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSIONS)
                } else {
                    openGallery()
                }
            } else {
                openGallery()
            }
        }

        // Set up take picture button
        takePictureButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSIONS)
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }
        }

        // Set up save button
        saveButton.setOnClickListener {
            val intent = Intent(this, OwnerDashboard::class.java)
            startActivity(intent)
            saveProfile()
        }

    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_UPLOAD)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun saveProfileImage() {

    }


    private fun saveProfile() {

        //grab currently signed in user's email
        var userName = FirebaseAuth.getInstance().currentUser?.email.toString()
        var userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        // Firebase Storage reference
        val storageRef = Firebase.storage.reference

        // Biography string to use
        val bio = bioEditText.text.toString()

        // Save profile picture and selected skills to user account
        val profilePicture = (profilePictureImageView.drawable as BitmapDrawable).bitmap // get the user's selected profile picture

        val selectedSkills = selectedSkillsList // get the user's selected skills

        //Obtain UserId by using userName to reverse locate through database and then proceed to store info under proper user
        getUidFromUserName(userName) { uid ->
            if (uid != null) {

                // Handle the uid value
                val userAccountRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

                // Store biography to user account
                val bioRef = userAccountRef.child("Biography")
                bioRef.setValue(bio)

                // Save the profile picture to Firebase Storage
                val profilePicturesRef = storageRef.child("images/${userID}.jpg")
                val profilePictureStream = ByteArrayOutputStream()
                profilePicture.compress(Bitmap.CompressFormat.JPEG, 100, profilePictureStream)
                val uploadTask = profilePicturesRef.putBytes(profilePictureStream.toByteArray())

                uploadTask.addOnSuccessListener {
                    profilePicturesRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save the download URL to the user's account node as a child node "profilePicture"
                        userAccountRef.child("profilePicture").setValue(uri.toString())
                        Toast.makeText(this, "Profile picture successfully uploaded", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show()
                }

                // Save the selected skills under the "Skills" field of the user's account node
                val skillsRef = userAccountRef.child("Skills")
                for (skill in selectedSkills) {
                    skillsRef.push().setValue(skill)
                        .addOnCompleteListener {
                            Toast.makeText(this, "Skills updated", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { err ->
                            Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                        }
                }
            } else {
                // Handle the case when no user with the matching userName was found
                println("User not found")
            }
        }
        // Finish activity and go back to previous screen
        finish()
    }

    /*
    Function to obtain Uid by using userName and returning Uid as a usable variable within lambda expression
     */
    private fun getUidFromUserName(userName: String, callback: (String?) -> Unit) {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")

        usersRef.orderByChild("userName").equalTo(userName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userNode = snapshot.children.first()
                    val uid = userNode.key
                    callback(uid) // Return the uid via the callback
                } else {
                    callback(null) // User with the matching userName not found, return null
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null) // Handle the error, return null
            }
        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        requestCode == REQUEST_IMAGE_CAPTURE -> openCamera()
                        requestCode == REQUEST_IMAGE_UPLOAD -> openGallery()
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    profilePictureImageView.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_UPLOAD -> {
                    try {
                        val imageUri = data?.data
                        val inputStream = imageUri?.let { contentResolver.openInputStream(it) }
                        val imageBitmap = BitmapFactory.decodeStream(inputStream)
                        profilePictureImageView.setImageBitmap(imageBitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }
}