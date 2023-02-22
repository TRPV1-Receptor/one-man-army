package com.example.onemanarmy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ReceiptCreatorActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReceiptAdapter
    var receiptList = mutableListOf<ReceiptItem>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_creator)

        receiptList.add(ReceiptItem("",0.0))
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ReceiptAdapter(mutableListOf(ReceiptItem("",0.0)))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val text = "Must provide atleast one service!"
        val duration = Toast.LENGTH_SHORT




        val backButton = findViewById<ImageView>(R.id.back)
        backButton.setOnClickListener {
            finish()
        }

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            adapter.addItem()
        }

        val removeButton = findViewById<Button>(R.id.removeButton)
        removeButton.setOnClickListener {
            if(adapter.itemCount == 1){
                Toast.makeText(applicationContext,text,duration).show()
            }
            else{
                adapter.removeItem()
            }

        }
    }
}

data class ReceiptItem(val serviceProvided:String, val cost:Double)

class ReceiptAdapter(private val items: MutableList<ReceiptItem>) : RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder>(){

    class ReceiptViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val serviceProvidedEditText: EditText = itemView.findViewById(R.id.serviceProvidedEditText)
        val costEditText: EditText = itemView.findViewById(R.id.costEditText)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_receipt, parent, false)
        return ReceiptViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val item = items[position]
        holder.serviceProvidedEditText.setText(item.serviceProvided)
        holder.costEditText.setText(item.cost.toString())
    }

    override fun getItemCount() = items.size

    fun addItem(){
        items.add(ReceiptItem("",0.0))
        notifyItemInserted(items.size -1)
    }

    fun removeItem(){
        items.removeLast()
        notifyItemRemoved(items.size)
    }
}