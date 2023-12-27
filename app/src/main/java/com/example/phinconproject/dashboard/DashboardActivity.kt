package com.example.phinconproject.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.phinconproject.R
import com.example.phinconproject.databinding.ActivityDashboardBinding
import com.example.phinconproject.viewModels.SharedViewModel

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sharedViewModel: SharedViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, HomeFragment.newInstance(sharedViewModel))
            addToBackStack(null) // Menambahkan HomeFragment ke dalam BackStack
            commit()
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        val bottomNav = binding.navView

        bottomNav.selectedItemId = R.id.navigation_home

        bottomNav.setOnItemSelectedListener { item ->
            val transaction = supportFragmentManager.beginTransaction()

            when (item.itemId) {
                R.id.navigation_home -> {
                    val fragment = HomeFragment()
                    transaction.replace(R.id.fragment_container, fragment)
                }
                R.id.navigation_history -> {
                    val fragment = HistoryFragment()
                    transaction.replace(R.id.fragment_container, fragment)
                }
                R.id.navigation_profile -> {
                    val fragment = ProfileFragment()
                    transaction.replace(R.id.fragment_container, fragment)
                }
            }
            transaction.addToBackStack(null)
            transaction.commit()
            true
        }
    }
}