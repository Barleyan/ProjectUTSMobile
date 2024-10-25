package com.barleyan.managementoko

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.appbar.MaterialToolbar

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            // Handle navigation icon press (if needed)
        }

        // Get buttons
        val btnCustomerList = findViewById<MaterialButton>(R.id.btnCustomerList)
        val btnProductList = findViewById<MaterialButton>(R.id.btnProductList)
        val btnTransactionList = findViewById<MaterialButton>(R.id.btnTransactionList)

        // Set onClickListeners
        btnCustomerList.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)  // Transition effect
        }

        btnProductList.setOnClickListener {
            val intent = Intent(this, MainActivityProduct::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btnTransactionList.setOnClickListener {
            val intent = Intent(this, MainActivityTransaction::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
