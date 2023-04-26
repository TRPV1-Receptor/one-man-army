package com.example.onemanarmy

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class AppointmentActivity : AppCompatActivity(){

    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:AppointmentAdapter
    private lateinit var calendar:MaterialCalendarView
    private var appointmentList = mutableListOf<Appointment>()
    private lateinit var currentUser : OwnerModel
    private lateinit var date : CalendarDay
    private lateinit var usersNode : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        //grabbing currently logged in user
        val userIntent = intent.extras
        currentUser = userIntent?.getSerializable("user") as OwnerModel

        //referencing database
        usersNode = FirebaseDatabase.getInstance().getReference("Users")

        //Obtaining references to my calendar and recyclerview
        calendar = findViewById(R.id.calendarView)
        recyclerView = findViewById(R.id.apptRecycler)

        //setting calendar to current day

        date = CalendarDay.today()
        calendar.selectedDate = date

        //Initializing the adapter and the layout manager
        adapter = AppointmentAdapter(mutableListOf(Appointment()))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.clear()
        retreive()

        //handle clicking on appointment item
        adapter.setOnItemClickListener(object : AppointmentAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val apppointment = adapter.getItem(position)
                openPopUp("hi",apppointment)
            }
        })

        //Keeps track of what date is selected for the appointment creation
        calendar.setOnDateChangedListener { _, date, _ ->
            updateData(date)
        }

        val addBtn = findViewById<FloatingActionButton>(R.id.addAppointment)
        addBtn.setOnClickListener{
                openPopUp("add")
            }
        }

    @SuppressLint("SetTextI18n")
    private fun openPopUp(type : String, appt : Appointment = Appointment()) {

        if (type == "add"){
            //Opening the popup and grabbing references to items on it
            val builder = AlertDialog.Builder(this)
            val popUpView = layoutInflater.inflate(R.layout.add_appointment_popup, null)
            val date = popUpView.findViewById<TextView>(R.id.date)
            val timepicker = popUpView.findViewById<TimePicker>(R.id.timePicker)

            //Placing pretty date on bottom of it.
            date.text =getPrettyDate(calendar)
            builder.setView(popUpView)

            builder.setPositiveButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }
            //Grabs all info from fields, makes an appointment object out of it, adds to
            builder.setNegativeButton("Create") { _, _ ->


                var flag = true

                val selectedDate = calendar.selectedDate
                val customerName = popUpView.findViewById<EditText>(R.id.customerName)
                val customerCity = popUpView.findViewById<EditText>(R.id.customerCity)
                val customerStreet = popUpView.findViewById<EditText>(R.id.customerStreet)
                val customerZip = popUpView.findViewById<EditText>(R.id.customerZip)

                when {
                    TextUtils.isEmpty(customerName.text.toString().trim()) -> {
                        customerName.error = "Required Field"
                        flag = false
                    }
                    TextUtils.isEmpty(customerCity.text.toString().trim()) -> {
                        customerCity.error = "Required Field"
                        flag = false
                    }
                    TextUtils.isEmpty(customerCity.text.toString().trim()) -> {
                        customerCity.error = "Required Field"
                        flag = false
                    }
                    TextUtils.isEmpty(customerZip.text.toString().trim()) -> {
                        customerZip.error = "Required Field"
                        flag = false
                    }
                }
                if (flag) {
                    val id = SimpleDateFormat("MMddyyyy_HHmmss", Locale.getDefault())
                        .format(System.currentTimeMillis())

                    val time = getTime(timepicker.hour, timepicker.minute)
                    val address = "${customerStreet.text.trim()}\n${customerCity.text.trim()},${customerZip.text.trim()}"

                    val appt = Appointment(customerName.text.toString().trim(), time, id, address, selectedDate)

                    save(appt)
                    appointmentList.add(appt)
                    updateData(selectedDate)

                    calendar.addDecorators(selectedDate?.let {
                        CurrentDayDecorator(
                            this@AppointmentActivity,
                            it
                        )
                    })
                }
            }
            builder.create().show()
        }else{
            val builder = AlertDialog.Builder(this)
            val popUpView = layoutInflater.inflate(R.layout.appointment_popup,null)

            val customerName = popUpView.findViewById<TextView>(R.id.appointmentName)
            customerName.text = getString(R.string.appointment_cust_name_popup)+ appt.title

            val customerAddress = popUpView.findViewById<TextView>(R.id.appointmentAddress)
            customerAddress.text = "Address\n" + appt.customerAddress

            val customerDate = popUpView.findViewById<TextView>(R.id.appointmentDate)
            customerDate.text = "Date\n" +
                    appt.date?.month.toString() + "/"+
                    appt.date?.day.toString() + "/" +
                    appt.date?.year.toString()

            val appointmentTime = popUpView.findViewById<TextView>(R.id.appointmentTime)
            appointmentTime.text = "Time\n" + appt.startTime

            builder.setView(popUpView)

            builder.setPositiveButton("Remove"){dialog, _ ->
                deleteAppointment(appt)
            }

            builder.setNegativeButton("Ok"){dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }

    private fun deleteAppointment(appt: Appointment) {
        val currentUserNode = usersNode.child(currentUser.userId.toString())
        val allAppointmentsNode = currentUserNode.child("Appointments")

        allAppointmentsNode.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (child in snapshot.children){
                        val key = child.key.toString()
                        val value = child.value as HashMap<String,String>
                        if (value["id"] == appt.id){
                            allAppointmentsNode.child(key).removeValue()
                            appointmentList.remove(appt)

                            updateData(null)
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }


    private fun retreive() {
        val currentUserNode = usersNode.child(currentUser.userId.toString())
        val allAppointmentsNode = currentUserNode.child("Appointments")

        allAppointmentsNode.addListenerForSingleValueEvent(object :ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (child in snapshot.children){
                        val temp = child.value as HashMap<String,String>

                        val year = temp["year"]
                        val month = temp["month"]
                        val day = temp["day"]

                        val appointment = Appointment(
                            temp["title"],
                            temp["startTime"],
                            temp["id"],
                            temp["customerAddress"],
                            CalendarDay.from(year!!.toInt(),month!!.toInt(),day!!.toInt()))

                        appointmentList.add(appointment)
                    }
                    appointmentList.forEach{appointment ->

                        updateData(appointment.date)

                        calendar.addDecorators(appointment.date?.let {
                            CurrentDayDecorator(
                                this@AppointmentActivity,
                                it
                            )
                        })
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun save(appt: Appointment) {
        val currentUserNode = usersNode.child(currentUser.userId.toString())
        val allAppointmentsNode = currentUserNode.child("Appointments")

        val appoint = AppointmentStore()
        with(appoint){
            this.id = appt.id.toString()
            this.customerAddress= appt.customerAddress.toString()
            this.title= appt.title.toString()
            this.startTime= appt.startTime.toString()
            this.day= appt.date?.day.toString()
            this.month= appt.date?.month.toString()
            this.year= appt.date?.year.toString()
        }
        allAppointmentsNode.push().setValue(appoint)
    }

    //Format the date to make it pretty for the appointment creator
    private fun getPrettyDate(calendar:MaterialCalendarView): String {
        return  "Date: " +
                calendar.selectedDate?.month.toString()+ "/" +
                calendar.selectedDate?.day.toString() + "/" +
                calendar.selectedDate?.year.toString()
    }

    private fun updateData(date:CalendarDay?){
        val filtered = appointmentList.filter { appointment ->
            appointment.date == date
        }
        adapter.updateData(filtered)
    }
}
    fun getTime(hour:Int,minute:Int): String {
        if (hour>12){
            if (minute<10){
                val newMin = "0$minute"
                val newHour = hour-12
                return "$newHour:$newMin PM"
            }else{
                val newHour = hour-12
                return "$newHour:$minute PM"
            }
        }else if  (hour == 0){
            return "12:$minute AM"
        } else{
            if (minute < 10){
                val newMin = "0$minute"
                return "$hour:$newMin AM"
            }else{
                return "$hour:$minute AM"
            }
        }
    }
    data class Appointment(
        var title:String? = "",
        var startTime:String? = "",
        var id:String?="",
        var customerAddress:String?="",
        var date:CalendarDay? =null):java.io.Serializable

    data class AppointmentStore(
        var title:String = "",
        var startTime:String = "",
        var id:String="",
        var customerAddress:String="",
        var day:String? =null,
        var month:String? = null,
        var year : String? = null):java.io.Serializable

