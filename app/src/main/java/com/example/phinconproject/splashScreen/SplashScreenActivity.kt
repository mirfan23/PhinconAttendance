package com.example.phinconproject.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.phinconproject.MainActivity
import com.example.phinconproject.databinding.ActivitySplashScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SplashScreenActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var splashImage: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashImage = Firebase.database.getReference("SplashScreen")

        getSplasScreenImage()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun getSplasScreenImage() {
        binding.apply {
            splashImage.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val image = snapshot.child("url").value.toString()

                    Glide.with(application).load(image).into(SplashScreenImage)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast
                        .makeText(
                            this@SplashScreenActivity,
                            "Error Loadng Image",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            })
        }
    }

}