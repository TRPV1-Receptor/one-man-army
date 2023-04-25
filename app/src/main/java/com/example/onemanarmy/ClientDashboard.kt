package com.example.onemanarmy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

class ClientDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_dashboard)

        val searchButton = findViewById<ConstraintLayout>(R.id.search)
        searchButton.setOnClickListener{
            val intent = Intent(this,SearchActivity::class.java)
            startActivity(intent)
        }

        val faqButton = findViewById<ConstraintLayout>(R.id.unknown)
        faqButton.setOnClickListener {
            val intent = Intent(this,FAQ::class.java)
            startActivity(intent)
        }
    }
}