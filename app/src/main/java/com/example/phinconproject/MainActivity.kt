package com.example.phinconproject

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.phinconproject.databinding.ActivityMainBinding
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.phinconproject.adapters.SliderAdapter
import com.example.phinconproject.auth.LoginActivity
import com.example.phinconproject.auth.RegisterActivity
import com.example.phinconproject.models.SliderItemModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    private val sliderItems = listOf(
        SliderItemModel(
            R.drawable.image_slider1,
            "DIGITAL ABSENSI",
            "Kehadiran sistem absensi digital merupakan penemuan yang mampu menggantikan pencatatan data kehadiran secara manual"
        ),
        SliderItemModel(
            R.drawable.image_slider2,
            "ATTENDANCE SYSTEM",
            "Pengelolaan karyawan di era digital yang baik, menghasilkan karyawan terbaik pula, salah satunya absensi karyawan"
        ),
        SliderItemModel(
            R.drawable.image_slider3,
            "SELALU PAKAI MASKER",
            "Guna mencegah penyebaran virus Covid-19, Pemerintah telah mengeluarkan kebijakan Physical Distancing serta kebijakan bekerja, belajar, dan beribadah dari rumah."
        )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("MainImageSlider")

        setupAnimation()
        imageSlider()
        intentToAuth()
    }

    private fun imageSlider() {
        binding.apply {
            val sliderAdapter = SliderAdapter(sliderItems)
            viewPager.adapter = sliderAdapter

            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.clipToPadding = false
            viewPager.clipChildren = false
            viewPager.offscreenPageLimit = 3
            viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val compositePageTransformer = ViewPager2.PageTransformer { page, position ->
                val r = 1 - Math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }
            viewPager.setPageTransformer(compositePageTransformer)

            val linearLayoutManager = LinearLayoutManager(this@MainActivity)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        }
    }


    @SuppressLint("Recycle")
    private fun setupAnimation() {
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playTogether(login, signup)
        }
    }

    private fun intentToAuth() {
        binding.apply {
            loginButton.setOnClickListener{
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                )
            }

            registerButton.setOnClickListener{
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                )
            }
        }
    }
}