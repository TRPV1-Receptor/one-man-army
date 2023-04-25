package com.example.onemanarmy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), FragmentNavigation  {

    companion object {
        private const val TAG = "KotlinActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.container,LoginFragment())
            .commit()

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