package com.example.onemanarmy
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View

class TIPS : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
    }
    public final fun showTIPS(view: View) {
        val intent = Intent(this, TIPS::class.java)
        startActivity(intent)
    }
}
