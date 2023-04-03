package com.example.onemanarmy

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ReceiptCreatorActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReceiptAdapter

    var receiptList = mutableListOf<ReceiptItem>()

    lateinit var bmp:Bitmap
    lateinit var scaledbmp:Bitmap

    private var PERMISSION_CODE = 101

    val text = "Must provide atleast one service!"
    private val duration = Toast.LENGTH_SHORT
    private val emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_creator)

        bmp = BitmapFactory.decodeResource(resources,R.drawable.onemanarmylogo)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)


        recyclerView = findViewById(R.id.recyclerView)
        adapter = ReceiptAdapter(mutableListOf(ReceiptItem("",0.0)))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

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
            val service = recyclerView[(recyclerView.layoutManager as LinearLayoutManager).childCount-1].findViewById<EditText>(R.id.serviceProvidedEditText)
            val cost = recyclerView[(recyclerView.layoutManager as LinearLayoutManager).childCount-1].findViewById<EditText>(R.id.costEditText)
            if(service.text.toString().isEmpty()){
                service.error = "Required Field"
            }else if (cost.text.toString().isEmpty()){
                cost.error = "Required Field"
            } else {
                receiptList.add(ReceiptItem(service.text.toString(),cost.text.toString().toDouble()))
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
                receiptList.removeLast()
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
            //Checking for empty fields
            if(checkAll()){
                Toast.makeText(applicationContext, "Please fill in all fields", duration).show()
            }else{
                //Permission Handling for External Storage
                if (checkPermissions()){
                    //Adding name and email as receipt objects for the purpose of accessing them in savepdf
                    receiptList = adapter.getItems()
                    receiptList.add(ReceiptItem(name,0.0))
                    receiptList.add(ReceiptItem(email,0.0))

                    if (adapter.itemCount!=0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            savePDF()
                            adapter.clear()
                        }
                    }
                }else{
                    requestPermission()
                }
            }
        }
    }
    //Function to check text boxes
    private fun checkAll():Boolean{
        for(item in recyclerView) {
            if (item.findViewById<EditText>(R.id.serviceProvidedEditText).text.toString().isEmpty()
                || item.findViewById<EditText>(R.id.costEditText).text.toString().isEmpty())
            {
                return true
            }
        }
        return false
    }

    private fun savePDF(){
        var email = receiptList.removeLast().serviceProvided
        var name = receiptList.removeLast().serviceProvided
        var emailArr = arrayOf(email)



        val pageHeight = 1120
        val pageWidth = 792

        val curTime = SimpleDateFormat("MMddyyyy_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val  date = SimpleDateFormat("MM-dd-yyyy",Locale.getDefault()).format(System.currentTimeMillis())

        val pdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()

        val myPageInfo = PdfDocument.PageInfo.Builder(pageWidth,pageHeight,1).create()

        val myPage = pdfDocument.startPage(myPageInfo)

        val canvas = myPage.canvas

        canvas.drawBitmap(scaledbmp,30F,30F,paint)

        title.typeface = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL)
        title.textSize = 30F
        title.color = ContextCompat.getColor(this,R.color.black)
        title.textAlign = Paint.Align.CENTER

        canvas.drawText("OneManArmy Receipt",400F,80F,title)


        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.textSize = 15F
        title.color = ContextCompat.getColor(this,R.color.Black)
        title.textAlign = Paint.Align.CENTER

        canvas.drawText("Thank you for using OneManArmy!",400F,105F,title)

        paint.strokeWidth = 40F
        paint.color = ContextCompat.getColor(this, R.color.Navy)
        canvas.drawLine(0F,0F,792F,0F,paint)
        canvas.drawLine(0F,1120F,792F,1120F,paint)
        paint.color = ContextCompat.getColor(this,R.color.black)
        paint.strokeWidth = 1F


        canvas.drawLine(360F,220F,440F,220F,paint)
        canvas.drawText(date,370F,215F,paint)


        title.typeface = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL)
        title.textSize = 18F
        title.color = ContextCompat.getColor(this,R.color.black)
        title.textAlign = Paint.Align.LEFT

        canvas.drawText("Company Name",40F,200F,paint)
        canvas.drawText("123 Main Street",40F,220F,paint)
        canvas.drawText("Greenville, NC 27858",40F,240F,paint)
        canvas.drawText("(321) 456-7890",40F,260F,paint)
        canvas.drawText("Email Address",40F,280F,paint)
        canvas.drawLine(300F,320F,500F,320F,paint)

        title.typeface = Typeface.create(Typeface.DEFAULT,Typeface.BOLD)
        canvas.drawText("BILL TO",380F,315F,title)
        title.typeface = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL)

        canvas.drawText(name,360F,355F,paint)
        canvas.drawText(email,360F,335F,paint)
        canvas.drawText("(321) 456-7890",360F,375F,paint)

        canvas.drawLine(40F,400F,740F,400F,paint)
        canvas.drawLine(40F,500F,740F,500F,paint)

        title.textAlign = Paint.Align.LEFT
        title.textSize = 50F
        canvas.drawText("Receipt Total",40F,470F,title)

        title.textSize = 20F
        title.textAlign = Paint.Align.LEFT

        title.typeface = Typeface.create(Typeface.DEFAULT,Typeface.BOLD)
        canvas.drawText("Item",40F,540F,title)
        title.typeface = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL)

        title.textAlign = Paint.Align.RIGHT
        title.typeface = Typeface.create(Typeface.DEFAULT,Typeface.BOLD)
        canvas.drawText("Amount",740F,540F,title)
        title.typeface = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL)


        var yCoord = 575F
        val itemXcoord = 40F
        val ammountXcoord = 740F
        var totalCost = 0.0


        for(item in receiptList){
            totalCost += item.cost
            title.textAlign = Paint.Align.LEFT
            canvas.drawText(item.serviceProvided,itemXcoord,yCoord,title)
            title.textAlign = Paint.Align.RIGHT
            canvas.drawText("$" + item.cost.toString(),ammountXcoord,yCoord,title)
            yCoord += 35F
        }

        val totalYcoord = yCoord+70F
        title.textAlign = Paint.Align.RIGHT
        title.textSize = 50F
        canvas.drawText("$${totalCost}",740F,470F,title)

        title.textAlign = Paint.Align.RIGHT
        title.textSize = 20F
        canvas.drawText("Total:",550F,totalYcoord,title)
        title.textAlign = Paint.Align.RIGHT
        canvas.drawText("$${totalCost}",ammountXcoord,totalYcoord,title)



        pdfDocument.finishPage(myPage)

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS.toString()),"Test${curTime}.PDF")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this,"PDF Generated!",duration).show()
            val intent = Intent(Intent.ACTION_SEND)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val path = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID + ".provider",file)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, emailArr)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Service Receipt")
            intent.putExtra(Intent.EXTRA_TEXT, "Dear $name,\n" +
                    "\n" +
                    "Thank you for your recent purchase at [Business Name]. We are writing to confirm that we have sent your receipt to the email address you provided.\n" +
                    "\n" +
                    "Your receipt is attached to this email as a PDF document. Please note that this is an official record of your transaction, so we recommend that you keep it in a safe place for future reference. If you have any questions or concerns about your receipt or your purchase, please don't hesitate to contact us.\n" +
                    "\n" +
                    "Once again, thank you for choosing [Business Name]. We look forward to serving you again in the future.\n" +
                    "\n" +
                    "Best regards,\n" +
                    "[Your Name]\n" +
                    "[Business Name]")
            intent.putExtra(Intent.EXTRA_STREAM,path)
            startActivity(Intent.createChooser(intent,"Send email..."))
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(this,"Oops, something went wrong.",duration).show()
        }

        pdfDocument.close()
    }

    private fun checkPermissions(): Boolean{

        val writeStoragePermission = ContextCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        )

        val readStoragePermission = ContextCompat.checkSelfPermission(
            this,
            READ_EXTERNAL_STORAGE
        )

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),PERMISSION_CODE
        )
    }


    //Permission Handling
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE){
            if(grantResults.isNotEmpty()){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    &&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission Granted",duration).show()
                }else{
                    Toast.makeText(this,"Permission Denied",duration).show()
                }
            }
        }
    }
}


data class ReceiptItem(var serviceProvided:String, var cost:Double)

class ReceiptAdapter(private val items: MutableList<ReceiptItem>) : RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder>(){

    class ReceiptViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var serviceProvidedEditText: EditText = itemView.findViewById(R.id.serviceProvidedEditText)
        val costEditText: EditText = itemView.findViewById(R.id.costEditText)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_receipt, parent, false)
        return ReceiptViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val item = items[position]
        holder.costEditText.setText("")
        holder.serviceProvidedEditText.setText("")

        holder.costEditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()){
                    item.cost = p0.toString().toDouble()
                }
            }
        })

        holder.serviceProvidedEditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                item.serviceProvided = p0.toString()
            }
        })
    }

    override fun getItemCount() = items.size

    fun getItems():MutableList<ReceiptItem>{
        val list = mutableListOf<ReceiptItem>()
        for(item in items){
            Log.d("Service Provided",item.serviceProvided)
            Log.d("Cost", item.cost.toString())
            val add = ReceiptItem(item.serviceProvided,item.cost)
            list.add(add)
        }
        return list
    }


    fun addItem(service:String=""){
        items.add(ReceiptItem(service,0.0))
        notifyItemInserted(items.size -1)
    }

    fun removeItem():ReceiptItem{
        val item = items.removeLast()
        notifyItemRemoved(items.size)
        return item
    }

    fun clear(){
        items.clear()
        notifyDataSetChanged()
        items.add(ReceiptItem("",0.0))
        notifyItemInserted(items.size -1)

    }
}
