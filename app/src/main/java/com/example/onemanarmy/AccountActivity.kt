package com.example.onemanarmy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class AccountActivity : AppCompatActivity() {

    private lateinit var saveData: SaveData
    override fun onCreate(savedInstanceState: Bundle?) {

        //share preference
        saveData = SaveData(this)
        if (saveData.loadDarkState() == true) {
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)


        val backBtn = findViewById<ImageView>(R.id.back)
        backBtn.setOnClickListener { finish() }
    }
}