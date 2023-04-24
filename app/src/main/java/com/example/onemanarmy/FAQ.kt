package com.example.onemanarmy
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View

class FAQ : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
    }
    public final fun showFAQ(view: View) {
        val intent = Intent(this, FAQ::class.java)
        startActivity(intent)
    }
}
