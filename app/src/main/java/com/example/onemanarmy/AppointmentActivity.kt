package com.example.onemanarmy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AppointmentActivity : AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:AppointmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        recyclerView = findViewById(R.id.apptRecycler)
        adapter = AppointmentAdapter(mutableListOf(Appointment()))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)




    }
}

data class Appointment(var title:String = "",var startTime:Int= 0,var endTime:Int=0,var customerName:String="",var customerAddress:String="", var date:Int=0)

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
        holder.appointmentTitle.text = "Appointment Slot"
    }

    override fun getItemCount() = items.size

    fun addItem(){
        items.add(Appointment("Fuck Me"))
        notifyItemInserted(items.size-1)
    }


}