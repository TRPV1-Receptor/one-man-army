package com.example.onemanarmy

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.fragment.app.Fragment
import android.content.Intent
import android.widget.RadioButton
import android.widget.RadioGroup


class MainActivity : AppCompatActivity(), FragmentNavigation  {



    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.container,LoginFragment())
            .commit()
        // Get the radio group
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        // Set a listener for the radio group
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // Get the selected radio button
            val radioButton = findViewById<RadioButton>(checkedId)

            // Check which radio button was selected
            when (radioButton.id) {
                R.id.clientRadioButton -> {
                    // Switch to the client activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.businessOwnerRadioButton -> {
                    // Switch to the business owner activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }



    override fun navigateFrag(fragment: Fragment, addToStack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if(addToStack){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}