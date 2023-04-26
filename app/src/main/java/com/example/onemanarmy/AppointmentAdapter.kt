package com.example.onemanarmy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter(private val items:MutableList<Appointment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position : Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    class AppointmentViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        var appointmentTitle : TextView = itemView.findViewById(R.id.appointTitle)
        var appointmentTime : TextView = itemView.findViewById(R.id.appointTime)
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    class EmptyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
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
                holder.appointmentTitle.text = item.title
                holder.appointmentTime.text = item.startTime
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