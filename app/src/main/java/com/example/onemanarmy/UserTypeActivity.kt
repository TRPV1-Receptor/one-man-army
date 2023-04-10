package com.example.onemanarmy

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity


class UserTypeActivity : AppCompatActivity() {
    private var userTypeRadioGroup: RadioGroup? = null
    private var continueButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_type)

        // Find views
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup)
        continueButton = findViewById(R.id.continueButton)

        // Set click listener for continue button
        continueButton?.setOnClickListener {
            val selectedId = userTypeRadioGroup?.checkedRadioButtonId
            if (selectedId == R.id.ownerRadioButton) {
                // User selected Owner
                //val intent = Intent(this, OwnerSignUpActivity::class.java)
                startActivity(intent)
                // TODO: Handle Owner selection
            } else if (selectedId == R.id.customerRadioButton) {
                // User selected Customer
                //val intent = Intent(this, CustomerDashboardActivity::class.java)
                startActivity(intent)
                // TODO: Handle Customer selection
            }
        }
    }
}