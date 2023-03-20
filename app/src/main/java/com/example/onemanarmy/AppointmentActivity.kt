package com.example.onemanarmy

import android.annotation.SuppressLint
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

class AppointmentActivity : AppCompatActivity(){

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

    @SuppressLint("SetTextI18n")
    private fun openPopUp(type : String, appt : Appointment = Appointment()) {

        if (type == "add"){
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

                val time = getTime(timepicker.hour,timepicker.minute)
                val address = "$customerStreet\n$customerCity,$customerZip"

                val appt = Appointment(customerName,time,customerName,address,selectedDate)
                appointmentList.add(appt)

                calendar.addDecorators(selectedDate?.let {
                    CurrentDayDecorator(this@AppointmentActivity,
                        it
                    )
                })
                updateData(appt.date)
            }
            builder.create().show()
        }else{
            val builder = AlertDialog.Builder(this)
            val popUpView = layoutInflater.inflate(R.layout.appointment_popup,null)

            val customerName = popUpView.findViewById<TextView>(R.id.appointmentName)
            customerName.text = getString(R.string.appointment_cust_name_popup).uppercase()+ appt.customerName

            val customerAddress = popUpView.findViewById<TextView>(R.id.appointmentAddress)
            customerAddress.text = "ADDRESS\n" + appt.customerAddress

            val customerDate = popUpView.findViewById<TextView>(R.id.appointmentDate)
            customerDate.text = "DATE\n" +
                                appt.date?.month.toString() + "/"+
                                appt.date?.day.toString() + "/" +
                                appt.date?.year.toString()

            val appointmentTime = popUpView.findViewById<TextView>(R.id.appointmentTime)
            appointmentTime.text = "TIME\n" + appt.startTime

            builder.setView(popUpView)

            builder.setPositiveButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }

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
        adapter.setOnItemClickListener(object : AppointmentAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val apppointment = adapter.getItem(position)
                openPopUp("hi",apppointment)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.clear()

        //Keeps track of what date is selected for the appointment creation
        calendar.setOnDateChangedListener { _, date, _ ->
            updateData(date)
        }

        val addBtn = findViewById<FloatingActionButton>(R.id.addAppointment)
        addBtn.setOnClickListener{
                openPopUp("add")
            }
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
    } else{
        if (minute < 10){
            val newMin = "0$minute"
            return "$hour:$newMin"
        }else{
            return "$hour:$minute AM"
        }
    }
}
data class Appointment(
    var title:String = "",
    var startTime:String = "",
    var customerName:String="",
    var customerAddress:String="",
    var date: CalendarDay? =null)

class AppointmentAdapter(private val items:MutableList<Appointment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position : Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    class AppointmentViewHolder(itemView:View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        var appointmentTitle : TextView = itemView.findViewById(R.id.appointTitle)
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
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
                AppointmentViewHolder(view,mListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is AppointmentViewHolder ->{
                val item = items[position]
                holder.appointmentTitle.text = item.title + "\n" + item.startTime
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

    fun getItem(position: Int): Appointment {
        return items[position]
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