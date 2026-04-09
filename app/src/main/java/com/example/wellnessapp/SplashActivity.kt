package com.example.wellnessapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logo)
        val glow = findViewById<ImageView>(R.id.glow)
        val text = findViewById<TextView>(R.id.splashText)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

        // Logo animation (pop + bounce)
        logo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1200)
            .setInterpolator(OvershootInterpolator())
            .start()

        // Glow pulse animation
        glow.animate()
            .alpha(0.6f)
            .scaleX(1.5f)
            .scaleY(1.5f)
            .setDuration(1500)
            .setStartDelay(300)
            .start()

        // Text fade in
        text.animate()
            .alpha(1f)
            .setDuration(1000)
            .translationY(0f)
            .start()

        // Move to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}