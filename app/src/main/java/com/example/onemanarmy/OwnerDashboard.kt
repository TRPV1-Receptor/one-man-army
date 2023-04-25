package com.example.onemanarmy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log


class OwnerDashboard : AppCompatActivity() {

    private lateinit var user : Map<String,String>
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_test)


        user = mapOf()
        var loggedIn = OwnerModel()

        usersRef.orderByChild("userName").equalTo(userEmail)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(child in snapshot.children){
                            user = child.value as Map<String, String>
                        }
                        loggedIn.firstName = user["firstName"]
                        loggedIn.lastName = user["lastName"]
                        loggedIn.serviceProvided= user["serviceProvided"]
                        loggedIn.businessName = user["businessName"]
                        loggedIn.businessPhone = user["businessPhone"]
                        loggedIn.businessEmail = user["businessEmail"]
                        loggedIn.businessAddress = user["businessAddress"]
                        loggedIn.userName = userEmail
                        loggedIn.userId = user["userId"]
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        val aptButton = findViewById<ConstraintLayout>(R.id.appointments)
        aptButton.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java,)
            intent.putExtra("user",loggedIn)
            startActivity(intent)

        }


        val receiptButton = findViewById<ConstraintLayout>(R.id.receiptcreator)
        receiptButton.setOnClickListener {
            val intent = Intent(this, ReceiptCreatorActivity::class.java,)
            intent.putExtra("user",loggedIn)
            startActivity(intent)

        }

        val accountButton = findViewById<ConstraintLayout>(R.id.account)
        accountButton.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java,)
            intent.putExtra("user",loggedIn)
            startActivity(intent)

        }

        val settingsButton = findViewById<ConstraintLayout>(R.id.settings)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java,)
            intent.putExtra("user",loggedIn)
            startActivity(intent)

        }

        val unknowButton = findViewById<ConstraintLayout>(R.id.unknown)
        unknowButton.setOnClickListener{
            Log.d("Owner",loggedIn.toString())
            Toast.makeText(this,loggedIn.toString(),Toast.LENGTH_SHORT).show()
        }
    }
}


