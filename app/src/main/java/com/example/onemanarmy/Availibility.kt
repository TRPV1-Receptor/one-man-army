package com.example.onemanarmy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

class Availibility : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_availibility)

        val mon_start = findViewById<Spinner>(R.id.mon_start)
        val mon_end = findViewById<Spinner>(R.id.mon_end)
        val tue_start = findViewById<Spinner>(R.id.tue_start)
        val tue_end = findViewById<Spinner>(R.id.tue_end)
        val wed_start = findViewById<Spinner>(R.id.wed_start)
        val wed_end = findViewById<Spinner>(R.id.wed_end)
        val thu_start = findViewById<Spinner>(R.id.thu_start)
        val thu_end = findViewById<Spinner>(R.id.thu_end)
        val fri_start = findViewById<Spinner>(R.id.fri_start)
        val fri_end = findViewById<Spinner>(R.id.fri_end)
        val sat_start = findViewById<Spinner>(R.id.sat_start)
        val sat_end = findViewById<Spinner>(R.id.sat_end)
        val sun_start = findViewById<Spinner>(R.id.sun_start)
        val sun_end = findViewById<Spinner>(R.id.sun_end)

        val test_but = findViewById<Button>(R.id.test_but)
        test_but.setOnClickListener{
            val intent = Intent(this, ProfileSetupActivity::class.java)
            startActivity(intent)
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.times_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mon_start.adapter = adapter
            mon_end.adapter = adapter
            tue_start.adapter = adapter
            tue_end.adapter = adapter
            wed_start.adapter = adapter
            wed_end.adapter = adapter
            thu_start.adapter = adapter
            thu_end.adapter = adapter
            fri_start.adapter = adapter
            fri_end.adapter = adapter
            sat_start.adapter = adapter
            sat_end.adapter = adapter
            sun_start.adapter = adapter
            sun_end.adapter = adapter
        }
    }
}