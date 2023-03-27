package com.example.onemanarmy

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import java.io.IOException


class DocScanner_FirstPage_Temp : AppCompatActivity() {
    private val REQUEST_CODE = 99
    private lateinit var scanButton:Button
    private lateinit var cameraButton:Button
    private lateinit var mediaButton: Button
    private lateinit var scannedImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_scanner_first_page_temp)
        init()

    }

    private fun init(){
        scanButton = findViewById(R.id.scanButton)
        scanButton.setOnClickListener(ScanButtonClickListener())
        cameraButton = findViewById(R.id.cameraButton)
        cameraButton.setOnClickListener(ScanButtonClickListener(ScanConstants.OPEN_CAMERA))
        mediaButton = findViewById(R.id.mediaButton)
        mediaButton.setOnClickListener(ScanButtonClickListener(ScanConstants.OPEN_MEDIA))
        scannedImageView = findViewById(R.id.scannedImage)
    }

    inner class ScanButtonClickListener(var preference: Int =0) :View.OnClickListener{

        init {
            this.preference = preference
        }
        override fun onClick(v: View) {
            startScan(preference)
        }
    }

    fun startScan(preference:Int) {
        val intent = Intent(this,ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE,preference)
        startActivityForResult(intent,REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val uri: Uri? = data?.extras?.getParcelable(ScanConstants.SCANNED_RESULT)
            val bitmap : Bitmap
            try {
                if (uri != null) {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                    contentResolver.delete(uri,null,null)
                    scannedImageView.setImageBitmap(bitmap)
                }
            }
            catch (e:IOException){
                e.printStackTrace()
            }
        }
    }

    open fun convertByteArrayToBitmap(data: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }
}

