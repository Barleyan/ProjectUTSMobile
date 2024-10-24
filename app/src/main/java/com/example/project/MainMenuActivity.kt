package com.barleyan.managementoko

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnCustomerList = findViewById<Button>(R.id.btnCustomerList)
        val btnProductList = findViewById<Button>(R.id.btnProductList)
        val btnTransactionList = findViewById<Button>(R.id.btnTransactionList)

        btnCustomerList.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnProductList.setOnClickListener {
            val intent = Intent(this, MainActivityProduct::class.java)
            startActivity(intent)
        }

        btnTransactionList.setOnClickListener {
            val intent = Intent(this, MainActivityTransaction::class.java)
            startActivity(intent)
        }
    }
}
