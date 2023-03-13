package com.example.onemanarmy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class AppointmentActivity : AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:AppointmentAdapter
    private lateinit var calendar:MaterialCalendarView

    var appointmentList = mutableListOf<Appointment>()

    private fun getDate(calendar:MaterialCalendarView): String {
        return  "Date: " +
                calendar.selectedDate?.month.toString()+ "/" +
                calendar.selectedDate?.day.toString() + "/" +
                calendar.selectedDate?.year.toString()
    }

    private fun openPopUp() {

        val builder = AlertDialog.Builder(this)
        val popUpView = layoutInflater.inflate(R.layout.add_appointment_popup, null)
        val date = popUpView.findViewById<TextView>(R.id.date)
        val timepicker = popUpView.findViewById<TimePicker>(R.id.timePicker)

        date.text =getDate(calendar)
        builder.setView(popUpView)

        builder.setPositiveButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }

        builder.setNegativeButton("Create"){dialog, _ ->
            val customerName = popUpView.findViewById<EditText>(R.id.customerName).text.toString()
            val customerCity = popUpView.findViewById<EditText>(R.id.customerCity).text.toString()
            val customerStreet = popUpView.findViewById<EditText>(R.id.customerStreet).text.toString()
            val customerZip = popUpView.findViewById<EditText>(R.id.customerZip).text.toString()
            val hour = timepicker.hour.toString()
            val minute = timepicker.minute.toString()
            val time = "$hour:$minute"
            val address = "$customerName\n$customerStreet\n$customerCity,$customerZip"

            val appt = Appointment(customerName,time,customerName,address,calendar.selectedDate)

            appointmentList.add(appt)
            adapter.addItem(appt)

            dialog.dismiss()
        }
        builder.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        recyclerView = findViewById(R.id.apptRecycler)
        adapter = AppointmentAdapter(mutableListOf(Appointment()))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        calendar = findViewById(R.id.calendarView)
        calendar.setOnDateChangedListener { widget, date, selected ->
            Toast.makeText(this,date.toString(),Toast.LENGTH_SHORT).show()
        }

        val addBtn = findViewById<FloatingActionButton>(R.id.addAppointment)
        addBtn.setOnClickListener{
                Toast.makeText(this,calendar.currentDate.toString(),Toast.LENGTH_LONG).show()
                openPopUp()
            }
        }
    }

data class Appointment(
    var title:String = "",
    var startTime:String = "",
    var customerName:String="",
    var customerAddress:String="",
    var date: CalendarDay? =null)

class AppointmentAdapter(private val items:MutableList<Appointment>) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>(){
    class AppointmentViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var appointmentTitle : TextView = itemView.findViewById(R.id.appointTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val item = items[position]
        holder.appointmentTitle.text = item.title
    }

    override fun getItemCount() = items.size

    fun addItem(appointment: Appointment){
        items.add(appointment)
        notifyItemInserted(items.size-1)
    }


}