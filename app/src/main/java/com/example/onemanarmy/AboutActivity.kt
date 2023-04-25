package com.example.onemanarmy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.onemanarmy.databinding.AboutBinding

class AboutActivity : AppCompatActivity(){
    private lateinit var binding: AboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AboutBinding.inflate(layoutInflater)
        setContentView(R.layout.about)


        val submit_Button = findViewById<View>(R.id.send_btn)
        submit_Button.setOnClickListener{
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
                Toast.makeText(this,"Required App is not installed",Toast.LENGTH_SHORT).show()
            }
        }
    }
}