    package com.barleyan.managementoko

    import android.app.AlertDialog
    import android.content.Intent
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.widget.Button
    import android.widget.EditText
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.Observer
    import androidx.lifecycle.ViewModelProvider
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.project.AppViewModel
    import com.example.project.Transaction
    import com.example.project.TransactionAdapter
    import com.google.android.material.floatingactionbutton.FloatingActionButton

    @Suppress("UNREACHABLE_CODE")
    class MainActivityTransaction : AppCompatActivity() {

        lateinit var appViewModel: AppViewModel
        private lateinit var transactionAdapter: TransactionAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_transaction)


            // Setup RecyclerView
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            // Initialize adapter with empty list
            transactionAdapter = TransactionAdapter(mutableListOf())
            recyclerView.adapter = transactionAdapter

            appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

            // Observing data and updating UI
            appViewModel.allTransactions.observe(this, Observer { transactions ->
                transactions?.let {
                    transactionAdapter.updateTransactions(it) // Nama metode yang benar
                    // Update RecyclerView with customer data
                }
            })

            // Tombol FloatingActionButton untuk menambah data
            val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
            fabAdd.setOnClickListener {
                showAddTransactionDialog() // Tampilkan dialog untuk menambah customer
            }
        }

        // Fungsi untuk menampilkan dialog penambahan customer baru
        private fun showAddTransactionDialog() {
            // Inflating the layout untuk dialog
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null)
            val produkIDInput = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
            val customerIDInput = dialogView.findViewById<EditText>(R.id.etCustomerID)
            val quantityIDInput = dialogView.findViewById<EditText>(R.id.etquantityID)
            val totalPriceIDInput = dialogView.findViewById<EditText>(R.id.ettotalPriceID)

            // Membuat dialog
            AlertDialog.Builder(this)
                .setTitle("Tambah Transaki")
                .setView(dialogView)
                .setPositiveButton("Tambah") { dialog, which ->
                    // Ketika tombol "Tambah" ditekan, ambil input dan simpan data baru
                    val ProdukID = produkIDInput.text.toString()
                    val CustomerID = customerIDInput.text.toString()
                    val QuantityID = quantityIDInput.text.toString()
                    val TotalpriceID = totalPriceIDInput.text.toString()

                    if (ProdukID.isNotEmpty() && CustomerID.isNotEmpty() && QuantityID.isNotEmpty()) {
                        val newTransaction = Transaction(
                            productId = ProdukID, customerId = CustomerID, quantity = QuantityID, totalPrice = TotalpriceID)
                        appViewModel.insertTransaction(newTransaction) // Menyimpan data ke ViewModel
                    }
                }
                .setNegativeButton("Batal", null)
                .create()
                .show()
        }


// Ensure updateTransactions is correctly defined in the adapter to handle data changes


        // Fungsi untuk menghapus pelanggan dari ViewModel
        fun deleteTransaction(transaction: Transaction) {
            appViewModel.deleteTransaction(transaction) // Memanggil fungsi delete di ViewModel
        }
    }
