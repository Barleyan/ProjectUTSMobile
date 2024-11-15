package com.barleyan.managementoko

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.AppViewModel
import com.example.project.Transaction
import com.example.project.TransactionAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivityTransaction : AppCompatActivity() {

    lateinit var appViewModel: AppViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        // Initialize RecyclerView and set GridLayoutManager with 2 columns
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Set up GridLayoutManager with 2 columns
        val gridLayoutManager = GridLayoutManager(this, 2)

        // Optionally, define how many spans an item should occupy
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // Example: Customize how many columns an item occupies
                return when (position % 3) {
                    0 -> 2 // Every third item takes up 2 columns
                    else -> 1 // Other items take up 1 column
                }
            }
        }

        recyclerView.layoutManager = gridLayoutManager

        // Initialize the adapter (no need for mutable list now)
        transactionAdapter = TransactionAdapter()
        recyclerView.adapter = transactionAdapter

        // Initialize ViewModel
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Observe changes to all transactions
        appViewModel.allTransactions.observe(this, Observer { transactions ->
            transactions?.let {
                transactionAdapter.submitList(it)  // Using submitList for updating the adapter
            }
        })

        // Floating Action Button for adding a new transaction
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddTransactionDialog()
        }
    }

    // Dialog for adding a new transaction
    private fun showAddTransactionDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null)
        val produkIDInput = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
        val customerIDInput = dialogView.findViewById<EditText>(R.id.etCustomerID)
        val quantityIDInput = dialogView.findViewById<EditText>(R.id.etQuantityID)
        val totalPriceIDInput = dialogView.findViewById<EditText>(R.id.etTotalPriceID)

        AlertDialog.Builder(this)
            .setTitle("Tambah Transaksi")
            .setView(dialogView)
            .setPositiveButton("Tambah") { dialog, which ->
                val ProdukID = produkIDInput.text.toString()
                val CustomerID = customerIDInput.text.toString()
                val QuantityID = quantityIDInput.text.toString()
                val TotalpriceID = totalPriceIDInput.text.toString()

                if (ProdukID.isNotEmpty() && CustomerID.isNotEmpty() && QuantityID.isNotEmpty()) {
                    val newTransaction = Transaction(
                        productId = ProdukID, customerId = CustomerID, quantity = QuantityID, totalPrice = TotalpriceID)
                    appViewModel.insertTransaction(newTransaction)
                }
            }
            .setNegativeButton("Batal", null)
            .create()
            .show()
    }

    // Optionally, you can use this method if you need to manually delete transactions
    fun deleteTransaction(transaction: Transaction) {
        appViewModel.deleteTransaction(transaction)
    }
}
