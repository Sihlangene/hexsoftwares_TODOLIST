package com.example.hexsoftwares_todolist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Find the ImageView to display the GIF
        val gifImageView: ImageView = findViewById(R.id.gifImageView)

        // Load the GIF using Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.splash_gif) // your GIF file located in res/drawable
            .into(gifImageView)

        // Handler to transition to the WelcomeActivity after a delay
        Handler().postDelayed({
            // Start MainActivity
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            // Close SplashActivity to prevent going back
            finish()
        }, 5000) // Show splash screen for 3 seconds
    }
}
