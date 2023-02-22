package com.example.onemanarmy

import android.content.ContentProvider
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Parameter


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
        val emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()

        val custName = findViewById<EditText>(R.id.customerName)
        val custEmail = findViewById<EditText>(R.id.customerEmail)

        //Buttion Initialization
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

        val createButton = findViewById<Button>(R.id.createButton)
        createButton.setOnClickListener {

            var bool = true

            val email = custEmail.text.toString()
            val name = custName.text.toString()

            //Make sure name is filled out
            if(custName!!.length() == 0){
                custName.error = "This field is required."
                bool = false
            }

            //Make sure email format is correct
            if(!emailRegex.matches(email)){
                custEmail.error = "Please enter valid email."
                bool = false
            }

            //Make sure Email is filled out
            if(custEmail!!.length() == 0){
                custEmail.error = "This field is required."
                bool = false
            }

            if(bool){

                val fileName = "${name}_receipt.pdf"
                val filePath = "${getExternalFilesDir(null)}/${fileName}"
                var totalCost = 0.0

                val document = Document()
                PdfWriter.getInstance(document, FileOutputStream(filePath))
                document.open()

                document.add(Paragraph("Customer Name: $custName"))

                for(item in receiptList){
                    document.add(Paragraph(item.serviceProvided + "\t\t\t" + item.cost))
                    totalCost += item.cost
                }

                document.add(Paragraph("Total Cost: $totalCost"))

                document.close()
                sendEmail(filePath, email)

            }
        }
    }

    private fun sendEmail(filePath:String, custEmail:String){
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", file)

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "application/pdf"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(custEmail))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Receipt for your transaction!")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Customer, Please find attached your receipt for the transaction. Best regards, Your Business.")
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri)

        startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }
}

data class ReceiptItem(val serviceProvided:String, var cost:Double)

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

