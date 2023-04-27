package com.example.onemanarmy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import com.example.onemanarmy.databinding.AboutBinding
import com.example.onemanarmy.databinding.ActivityMainBinding
import com.example.onemanarmy.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class SettingsActivity : AppCompatActivity() {

    private lateinit var saveData: SaveData
    private lateinit var binding: AboutBinding
    private lateinit var binding2: ActivitySettingsBinding

    private lateinit var currentUser : OwnerModel



    companion object {
        val IMAGE_REQUEST_CODE = 1_000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //share preference
        saveData = SaveData(this)
        if (saveData.loadDarkState() == true) {
            setTheme(R.style.AppTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)

        val userIntent = intent.extras
        currentUser = userIntent?.getSerializable("user") as OwnerModel

        binding2 = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding2.root)

        binding2.pickImageButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            FirebaseAuth.getInstance().signOut()
            startActivity(intent)

        }



        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener { finish() }
        ///////////////////////////////////////////
        val secondButton = findViewById<View>(R.id.send_message)
        secondButton.setOnClickListener{

            val Intent = Intent(this,AboutActivity::class.java)
            startActivity(Intent)
        }

        ////////////////////////////////////////////











        val contactUs :ImageView = findViewById<ImageView>(R.id.contactUs)




        // Apply OnClickListener  to imageView to
        // switch from one activity to another

        // Apply OnClickListener  to imageView to
        // switch from one activity to another
        contactUs.setOnClickListener(View.OnClickListener { // Intent class will help to go to next activity using
            // it's object named intent.
            // SecondActivty is the name of new created EmptyActivity.
            val intent = Intent(this,AboutActivity::class.java)
            startActivity(intent)
        })



    }


    private fun restartApplication() {
        val i = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            binding2.imageView.setImageURI(data?.data)
        }
    }
}
