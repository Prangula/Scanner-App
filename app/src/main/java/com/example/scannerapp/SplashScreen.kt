package com.example.scannerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.scannerapp.ui.scanner.ScannerFragment

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler().postDelayed({

            val intent = Intent(this,Fragment::class.java)
            startActivity(intent)
            finish()

        },2500)

    }
}