package com.example.onemanarmy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*

class AvailabilityActivity : Activity() {

    private lateinit var datePicker: DatePicker
    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker
    private lateinit var holidayCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_availability_acivity)

        datePicker = findViewById(R.id.datePicker)
        startTimePicker = findViewById(R.id.startTimePicker)
        endTimePicker = findViewById(R.id.endTimePicker)
        holidayCheckBox = findViewById(R.id.holidayCheckBox)

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            // Get the selected date, start time, and end time
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth
            val startHour = startTimePicker.hour
            val startMinute = startTimePicker.minute
            val endHour = endTimePicker.hour
            val endMinute = endTimePicker.minute

            // Create a Calendar object with the selected date and times
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day, startHour, startMinute)

            // Store the availability into the user's account
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putLong("availability_${calendar.timeInMillis}_start", calendar.timeInMillis)
            calendar.set(year, month, day, endHour, endMinute)
            editor.putLong("availability_${calendar.timeInMillis}_end", calendar.timeInMillis)
            editor.putBoolean("availability_${calendar.timeInMillis}_holiday", holidayCheckBox.isChecked)
            editor.apply()

            // Finish the activity and return to the previous screen
            setResult(RESULT_OK, Intent())
            finish()
        }
    }
}