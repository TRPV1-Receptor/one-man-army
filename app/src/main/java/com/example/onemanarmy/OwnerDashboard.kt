package com.example.onemanarmy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController


class OwnerDashboard : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var saveData: SaveData

    override fun onCreate(savedInstanceState: Bundle?) {

        // Dre- This is my code for saving dark theme colors
        saveData = SaveData(this)
        if (saveData.loadDarkState() == true) {
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.owner_dashboard)



        val aptButton = findViewById<CardView>(R.id.appointments)
        aptButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java,)
            startActivity(intent)

        })

        val docButton = findViewById<CardView>(R.id.documentscanner)
        docButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DocScannerActivity::class.java,)
            startActivity(intent)

        })

        val receiptButton = findViewById<CardView>(R.id.receiptcreator)
        receiptButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ReceiptCreatorActivity::class.java,)
            startActivity(intent)

        })

        val accountButton = findViewById<CardView>(R.id.account)
        accountButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AccountActivity::class.java,)
            startActivity(intent)

        })

        val settingsButton = findViewById<CardView>(R.id.settings)
        settingsButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SettingsActivity::class.java,)
            startActivity(intent)

        })
    }

}


