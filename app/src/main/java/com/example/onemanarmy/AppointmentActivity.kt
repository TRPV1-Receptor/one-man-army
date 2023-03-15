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
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener

class AppointmentActivity : AppCompatActivity(),OnDateSelectedListener{

    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:AppointmentAdapter
    private lateinit var calendar:MaterialCalendarView


    private var appointmentList = mutableListOf<Appointment>()

    //Format the date to make it pretty for the appointment creator
    private fun getDate(calendar:MaterialCalendarView): String {
        return  "Date: " +
                calendar.selectedDate?.month.toString()+ "/" +
                calendar.selectedDate?.day.toString() + "/" +
                calendar.selectedDate?.year.toString()
    }

    private fun openPopUp() {
        //Opening the popup and grabbing references to items on it
        val builder = AlertDialog.Builder(this)
        val popUpView = layoutInflater.inflate(R.layout.add_appointment_popup, null)
        val date = popUpView.findViewById<TextView>(R.id.date)
        val timepicker = popUpView.findViewById<TimePicker>(R.id.timePicker)

        //Placing pretty date on bottom of it.
        date.text =getDate(calendar)
        builder.setView(popUpView)

        builder.setPositiveButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }
        //Grabs all info from fields, makes an appointment object out of it, adds to
        builder.setNegativeButton("Create"){ _, _ ->
            val selectedDate = calendar.selectedDate
            val customerName = popUpView.findViewById<EditText>(R.id.customerName).text.toString()
            val customerCity = popUpView.findViewById<EditText>(R.id.customerCity).text.toString()
            val customerStreet = popUpView.findViewById<EditText>(R.id.customerStreet).text.toString()
            val customerZip = popUpView.findViewById<EditText>(R.id.customerZip).text.toString()
            val hour = timepicker.hour.toString()
            val minute = timepicker.minute.toString()
            val time = "$hour:$minute"
            val address = "$customerName\n$customerStreet\n$customerCity,$customerZip"

            val appt = Appointment(customerName,time,customerName,address,selectedDate)
            appointmentList.add(appt)

            updateData(appt.date)

        }
        builder.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        //Obtaining references to my calendar and recyclerview
        calendar = findViewById(R.id.calendarView)
        recyclerView = findViewById(R.id.apptRecycler)

        //Initializing the adapter and the layout manager
        adapter = AppointmentAdapter(mutableListOf(Appointment()))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.clear()

        //Keeps track of what date is selected for the appointment creation
        calendar.setOnDateChangedListener { _, date, _ ->
            updateData(date)
        }

        val addBtn = findViewById<FloatingActionButton>(R.id.addAppointment)
        addBtn.setOnClickListener{
                openPopUp()
            }
        }

    private fun updateData(date:CalendarDay?){
        val filtered = appointmentList.filter { appointment ->
            appointment.date == date
        }
        adapter.updateData(filtered)
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        Toast.makeText(this,"Hello There",Toast.LENGTH_SHORT).show()
    }
}



data class Appointment(
    var title:String = "",
    var startTime:String = "",
    var customerName:String="",
    var customerAddress:String="",
    var date: CalendarDay? =null)

class AppointmentAdapter(private val items:MutableList<Appointment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class AppointmentViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var appointmentTitle : TextView = itemView.findViewById(R.id.appointTitle)
    }

    class EmptyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var emptyView : TextView = itemView.findViewById(R.id.empty_view_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EMPTY ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.appointment_empty,parent,false)
                EmptyViewHolder(view)
            }
            else ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
                AppointmentViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is AppointmentViewHolder ->{
                val item = items[position]
                holder.appointmentTitle.text = item.title
            }
            is EmptyViewHolder ->{
                holder.emptyView.text ="There are no appointments today"
            }
        }
    }



    override fun getItemViewType(position: Int): Int {
        return if(items.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_APPOINT
    }
    companion object{
        private const val VIEW_TYPE_EMPTY = 0
        private const val VIEW_TYPE_APPOINT = 1
    }

    override fun getItemCount():Int {
        return if(items.isEmpty()) {
            0
        } else {
            items.size
        }
    }

    fun addItem(appointment: Appointment){
        items.add(appointment)
        notifyItemInserted(items.size-1)

    }
    private fun removeItem(){
        items.removeLast()
        notifyItemRemoved(items.size)
    }

    fun clear(){
        repeat(items.size){
            removeItem()
        }
    }
    fun updateData(list: List<Appointment>){
        if(items.size>0){
            clear()
        }
        for (item in list){
            addItem(item)
        }
    }
}