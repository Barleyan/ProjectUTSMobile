package com.barleyan.managementoko

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hapus logika untuk memeriksa status PIN
        setContentView(R.layout.activity_main)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Set fragment awal saat aplikasi diluncurkan
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.navigation_home // Mengatur default item
        }

        // Menangani navigasi pada BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment = when (item.itemId) {
                R.id.navigation_home -> HomeFragment()
                R.id.navigation_customer -> CustomerFragment()
                R.id.navigation_product -> ProductFragment()
                R.id.navigation_transaction -> TransactionFragment()
                R.id.navigation_settings -> SettingsFragment()
                else -> null
            }
            selectedFragment?.let {
                loadFragment(it)
                true
            } ?: false
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit() // Tidak menambahkan ke backstack untuk BottomNavigation
    }
}
