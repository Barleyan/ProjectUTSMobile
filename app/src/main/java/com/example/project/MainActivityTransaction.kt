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


            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            transactionAdapter = TransactionAdapter(mutableListOf())
            recyclerView.adapter = transactionAdapter

            appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

            appViewModel.allTransactions.observe(this, Observer { transactions ->
                transactions?.let {
                    transactionAdapter.updateTransactions(it)

                }
            })

            val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
            fabAdd.setOnClickListener {
                showAddTransactionDialog()
            }
        }


        private fun showAddTransactionDialog() {

            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null)
            val produkIDInput = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
            val customerIDInput = dialogView.findViewById<EditText>(R.id.etCustomerID)
            val quantityIDInput = dialogView.findViewById<EditText>(R.id.etQuantityID)
            val totalPriceIDInput = dialogView.findViewById<EditText>(R.id.etTotalPriceID)


            AlertDialog.Builder(this)
                .setTitle("Tambah Transaki")
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




        fun deleteTransaction(transaction: Transaction) {
            appViewModel.deleteTransaction(transaction)
        }
    }
