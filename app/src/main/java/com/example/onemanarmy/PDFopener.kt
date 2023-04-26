package com.example.onemanarmy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView

class PDFopener : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfopener)

        val myPDFviewer = findViewById<PDFView>(R.id.pdfViewer)

        val pdf = intent.getByteArrayExtra("pdf")
        myPDFviewer.fromBytes(pdf).load()


    }
}