package com.example.onemanarmy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.util.Log
import android.widget.Toast
import com.example.onemanarmy.databinding.ActivityReceiptCreatorScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class ReceiptCreatorScreen : AppCompatActivity() {
    private lateinit var binding:ActivityReceiptCreatorScreenBinding
    private lateinit var currentUser : OwnerModel
    private lateinit var receiptList : ArrayList<Pair<String,String>>
    private val database = FirebaseDatabase.getInstance()
    private val usersNode = database.getReference("Users")
    private val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptCreatorScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userIntent = intent.extras
        currentUser = userIntent?.getSerializable("user") as OwnerModel

        receiptList = ArrayList()

        binding.backButton.setOnClickListener{
            finish()
        }

        //Grabbing all the url for receipts from user
        val currentUserNode = usersNode.child(currentUser.userId.toString())
        val currentUserReceiptsNode = currentUserNode.child("Receipts")

        currentUserReceiptsNode
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (child in snapshot.children){
                            val info = Pair(child.key.toString(),child.value.toString())
                            receiptList.add(info)
                        }
                        if (receiptList.isNotEmpty()){
                            setAdapter(receiptList)
                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun setAdapter(receiptList : ArrayList<Pair<String,String>>){
        val adapter = ReceiptScreenAdapter(this,receiptList)

        binding.receiptListView.adapter = adapter
        binding.receiptListView.isClickable = true

        binding.receiptListView.setOnItemClickListener { adapterView, view, position, l ->

            val itemClicked = adapterView.getItemAtPosition(position) as Pair<String,String>

            val gsReference = storage.getReferenceFromUrl(itemClicked.second)

            val ONE_MEGABYTE: Long = 1024 * 1024

            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { array ->
                val intent = Intent(applicationContext,PDFopener::class.java)
                intent.putExtra("pdf", array)
                startActivity(intent)
            }
        }
    }
}


