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


class SettingsActivity : AppCompatActivity() {

    private var switch: Switch? = null
    private lateinit var saveData: SaveData
    private lateinit var binding: AboutBinding
    private lateinit var binding2: ActivitySettingsBinding


    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        //share preference
        saveData = SaveData(this)
        if (saveData.loadDarkState() == true) {
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)


        /////////////This code is for contact form////////////



 binding = AboutBinding.inflate(layoutInflater)
       setContentView(binding.root)
 binding2 = ActivitySettingsBinding.inflate(layoutInflater)


            binding2.Aboutus.setOnClickListener{
                binding2 = ActivitySettingsBinding.inflate(layoutInflater)
            setContentView(R.layout.about)
            val email = binding.emailAddress.text.toString()
            val subject = binding.subject.text.toString()
            val message = binding.subject.text.toString()

            val addresses = email.split(",".toRegex()).toTypedArray()

            val intent = Intent(Intent.ACTION_SENDTO).apply {

                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL,addresses)
                putExtra(Intent.EXTRA_SUBJECT,subject)
                putExtra(Intent.EXTRA_TEXT,message)
            }
            if(intent.resolveActivity(packageManager)!= null){

                startActivity(intent)
            }else{
                Toast.makeText(this,"Required App is not installed", Toast.LENGTH_SHORT).show()
            }
        }


        /////////This code is for contact form^///////////////////////


        ///////////////////this code is for image upload
        binding2 = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding2.root)

        binding2.pickImageButton.setOnClickListener {
            pickImageFromGallery()
        }


        //////////////////////////////Dark Mode
        switch = findViewById<View>(R.id.switch_btn) as Switch?
        if (saveData.loadDarkState() == true) {
            switch?.isChecked = true
        }

        //on click ON OFf
        switch = findViewById<View>(R.id.switch_btn) as Switch?
        if (saveData.loadDarkState() == true) {
            switch?.isChecked = true
        }
        switch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                saveData.setDarkModeState(true)
                restartApplication()
            } else {
                saveData.setDarkModeState(false)
                restartApplication()

            }
        }
        ////////This code is for the back button///////////////
//        setContentView(R.layout.activity_settings)
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener { finish() }
        ///////////////////////////////////////////
//        val secondButton = findViewById<ImageView>(R.id.Aboutus)
//        secondButton.setOnClickListener{
//
//         val Intent = Intent(this,AboutActivity::class.java)
//        startActivity(Intent)
//            finish()
//        }

        ////////////////////////////////////////////




    }


    private fun restartApplication() {
        val i = Intent(applicationContext, MainActivity::class.java)
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
