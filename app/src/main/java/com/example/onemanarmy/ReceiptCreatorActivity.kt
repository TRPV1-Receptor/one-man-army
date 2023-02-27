package com.example.onemanarmy

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ReceiptCreatorActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReceiptAdapter
    var receiptList = mutableListOf<ReceiptItem>()
    var receiptListCount = 0
    private val storageCode = 1001
    var image = "app/src/main/res/drawable/onemanarmylogo.png"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_creator)

        //receiptList.add(ReceiptItem("",0.0))
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ReceiptAdapter(mutableListOf(ReceiptItem("",0.0)))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val text = "Must provide atleast one service!"
        val duration = Toast.LENGTH_SHORT
        val emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()

        val custName = findViewById<EditText>(R.id.customerName)
        val custEmail = findViewById<EditText>(R.id.customerEmail)


        //Button Initialization
        val backButton = findViewById<ImageView>(R.id.back)
        backButton.setOnClickListener {
            finish()
        }

        //checks if text boxes are empty before adding another one
        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val service = recyclerView[receiptListCount].findViewById<EditText>(R.id.serviceProvidedEditText)
            val cost = recyclerView[receiptListCount].findViewById<EditText>(R.id.costEditText)

            if(service.text.toString().isNullOrEmpty()){
                service.error = "Required Field"
            }else if (cost.text.toString().isNullOrEmpty()){
                cost.error = "Required Field"
            } else {
                receiptListCount++
                adapter.addItem()
            }


        }

        //removes the most recent service added and updates the list count
        val removeButton = findViewById<Button>(R.id.removeButton)
        removeButton.setOnClickListener {
            if(adapter.itemCount == 1){
                Toast.makeText(applicationContext,text,duration).show()
            }
            else{
                receiptListCount--
                adapter.removeItem()
            }
        }


        // Error handling, Populates ReceiptList with receipt items based on info in edit texts
        val createButton = findViewById<Button>(R.id.createButton)
        createButton.setOnClickListener {

            val email = custEmail.text.toString()
            val name = custName.text.toString()

            if (custName!!.length() == 0)
            {
                custName.error = "This field is required."
            }
            if(!emailRegex.matches(email))
            {
                custEmail.error = "Please enter valid email."
            }
            if (custEmail.length() == 0)
            {
                custEmail.error = "This field is required."
            }
            for(item in recyclerView){
                //If any text boxes are empty will notify user to fill them all in.
                if(item.findViewById<EditText>(R.id.serviceProvidedEditText).text.toString().isNullOrEmpty()
                    || item.findViewById<EditText>(R.id.costEditText).text.toString().isNullOrEmpty())
                {
                    Toast.makeText(applicationContext,"Please fill in all fields", duration).show()
                    receiptList.clear()
                    break
                }
                //Puts the info into receipt item objects and populates Receipt List
                receiptList.add(ReceiptItem(item.findViewById<EditText>(R.id.serviceProvidedEditText).text.toString(),item.findViewById<EditText>(R.id.costEditText).text.toString().toDouble()))

                Log.d("Service",item.findViewById<EditText>(R.id.serviceProvidedEditText).text.toString())
                Log.d("Cost",item.findViewById<EditText>(R.id.costEditText).text.toString())
            }
            receiptList.add(ReceiptItem(name,0.0))
            receiptList.add(ReceiptItem(email,0.0))

            //Permission Handling for External Storage
            if (!receiptList.isNullOrEmpty()) {
                if (Build.VERSION.SDK_INT > 24) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        requestPermissions(permission, storageCode)
                    } else {
                        savePDF()
                    }
                } else {
                    savePDF()
                }
            }
        }

    }
    private fun savePDF(){
        val email = receiptList.removeLast().serviceProvided
        val name = receiptList.removeLast().serviceProvided


        val cFileName = SimpleDateFormat("MMddyyyy_HHmmss", Locale.getDefault())
            .format(System.currentTimeMillis())
        val cFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + cFileName + ".pdf"


        val cReceipt = Document()


        try {
            PdfWriter.getInstance(cReceipt,FileOutputStream(cFilePath))
            cReceipt.open()

            val t = Paragraph()
            val table = PdfPTable(2)

            t.alignment = Element.ALIGN_LEFT
            val titleChunk = Chunk("Receipt", Font(Font.FontFamily.TIMES_ROMAN,24.0f, Font.BOLD))

            // TODO("Business Information needs to be grabbed from the Users Account")
            val from = Phrase("FROM", Font(Font.FontFamily.TIMES_ROMAN,12f,Font.BOLD.and(Font.UNDERLINE)))
            val to = Phrase("TO", Font(Font.FontFamily.TIMES_ROMAN,12f,Font.BOLD.and(Font.UNDERLINE)))

            val busChunk = Chunk(" Business Name \n Business Email\n Business Phone \n Date: ${SimpleDateFormat("MM/dd/yyy",Locale.getDefault()).format(System.currentTimeMillis())}")
            val cusChunk = Chunk(" Customer Name\n Customer Email")

            t.add(titleChunk)
            t.add("\n\n")

            t.add(from)
            t.add("\n")

            t.add(busChunk)
            t.add("\n\n")

            t.add(to)
            t.add("\n")

            t.add(cusChunk)
            t.add("\n\n\n")

            cReceipt.add(t)

            table.defaultCell.verticalAlignment = Element.ALIGN_CENTER
            table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER

            table.addCell(Phrase("Service Done",Font(Font.FontFamily.TIMES_ROMAN,20.0f)))
            table.addCell(Phrase("Cost", Font(Font.FontFamily.TIMES_ROMAN,20.0f)))

            table.defaultCell.verticalAlignment = Element.ALIGN_LEFT
            table.defaultCell.horizontalAlignment = Element.ALIGN_LEFT

            var totalCost = 0.0

            for(item in receiptList){
                totalCost += item.cost
                table.addCell(Phrase(item.serviceProvided, Font(Font.FontFamily.TIMES_ROMAN,18.0f)))
                table.addCell(Phrase(item.cost.toString(),Font(Font.FontFamily.TIMES_ROMAN,18.0f)))
            }


            table.addCell(Phrase("Total Cost", Font(Font.FontFamily.TIMES_ROMAN,18.0f,Font.BOLD)))
            table.addCell(Phrase(totalCost.toString(), Font(Font.FontFamily.TIMES_ROMAN,18.0f,Font.BOLD)))

            cReceipt.add(table)

            cReceipt.close()

            Toast.makeText(this,"$cReceipt.pdf\n is created to \n$cFilePath",Toast.LENGTH_SHORT).show()

        }catch (e: Exception){
            Log.d("Exception", e.toString())
            receiptList.clear()
            Toast.makeText(this,""+ e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //Permission Handling
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            storageCode -> {
                if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    savePDF()
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}//End of AppCompatActivity

data class ReceiptItem(var serviceProvided:String, var cost:Double)

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
        holder.costEditText.setText("")
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

