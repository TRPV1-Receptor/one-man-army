package com.example.onemanarmy

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.ui.AppBarConfiguration


class OwnerDashboard : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owner_dashboard)



        val aptButton = findViewById<ConstraintLayout>(R.id.appointments)
        aptButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java,)
            startActivity(intent)

        })

        val docButton = findViewById<ConstraintLayout>(R.id.documentscanner)
        docButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DocScannerActivity::class.java,)
            startActivity(intent)

        })

        val receiptButton = findViewById<ConstraintLayout>(R.id.receiptcreator)
        receiptButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ReceiptCreatorActivity::class.java,)
            startActivity(intent)

        })

        val accountButton = findViewById<ConstraintLayout>(R.id.account)
        accountButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AccountActivity::class.java,)
            startActivity(intent)

        })

        val settingsButton = findViewById<ConstraintLayout>(R.id.settings)
        settingsButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SettingsActivity::class.java,)
            startActivity(intent)

        })
    }

}


