package com.example.onemanarmy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth


class OwnerDashboard : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_test)



        val aptButton = findViewById<ConstraintLayout>(R.id.appointments)
        aptButton.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java,)
            startActivity(intent)

        }


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
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java,)
            startActivity(intent)

        }

        val unknowButton = findViewById<ConstraintLayout>(R.id.unknown)
        unknowButton.setOnClickListener{
            val userID = FirebaseAuth.getInstance().currentUser?.uid
            Toast.makeText(this,userID,Toast.LENGTH_SHORT).show()
        }
    }

}


