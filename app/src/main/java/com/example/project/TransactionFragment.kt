package com.barleyan.managementoko

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.project.AppViewModel
import com.example.project.Transaction

class TransactionFragment : Fragment() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        setupRecyclerView(recyclerView)

        // Initialize ViewModel
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Observe transaction data from ViewModel
        appViewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            transactions?.let {
                transactionAdapter.submitList(it) // Update the adapter's list
            }
        }

        // Setup FloatingActionButton for adding new transactions
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddTransactionDialog()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        // Use a GridLayoutManager with 2 columns
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager

        // Initialize the adapter and pass the context correctly
        transactionAdapter = TransactionAdapter(
            requireContext(), // Pass the context here
            onDelete = { transaction ->
                appViewModel.deleteTransaction(transaction)
                Toast.makeText(requireContext(), "Transaksi ${transaction.productId} telah dihapus", Toast.LENGTH_SHORT).show()
            },
            onEdit = { transaction ->
                showEditTransactionDialog(transaction)
            }
        )
        recyclerView.adapter = transactionAdapter
    }

    private fun showAddTransactionDialog() {
        // Inflate the custom dialog view
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_transaction, null)
        val productIDInput = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
        val customerIDInput = dialogView.findViewById<EditText>(R.id.etCustomerID)
        val quantityIDInput = dialogView.findViewById<EditText>(R.id.etQuantityID)
        val totalPriceIDInput = dialogView.findViewById<EditText>(R.id.etTotalPriceID)

        // Build the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Tambah Transaksi")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ -> }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()

        // Validate input before adding a transaction
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val productId = productIDInput.text.toString().trim()
            val customerId = customerIDInput.text.toString().trim()
            val quantity = quantityIDInput.text.toString().trim()
            val totalPrice = totalPriceIDInput.text.toString().trim()

            if (productId.isNotEmpty() && customerId.isNotEmpty() && quantity.isNotEmpty()) {
                val newTransaction = Transaction(
                    productId = productId, customerId = customerId, quantity = quantity, totalPrice = totalPrice)
                appViewModel.insertTransaction(newTransaction)
                dialog.dismiss()
            } else {
                // Show error messages
                if (productId.isEmpty()) productIDInput.error = "ID Produk tidak boleh kosong"
                if (customerId.isEmpty()) customerIDInput.error = "ID Customer tidak boleh kosong"
                if (quantity.isEmpty()) quantityIDInput.error = "Quantity tidak boleh kosong"
            }
        }
    }

    private fun showEditTransactionDialog(transaction: Transaction) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Transaksi")

        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_add_transaction, null)
        builder.setView(dialogView)

        val editProductId = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
        val editCustomerId = dialogView.findViewById<EditText>(R.id.etCustomerID)
        val editQuantity = dialogView.findViewById<EditText>(R.id.etQuantityID)
        val editTotalPrice = dialogView.findViewById<EditText>(R.id.etTotalPriceID)

        // Pre-fill the fields with current transaction data
        editProductId.setText(transaction.productId)
        editCustomerId.setText(transaction.customerId)
        editQuantity.setText(transaction.quantity)
        editTotalPrice.setText(transaction.totalPrice)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val newProductId = editProductId.text.toString().trim()
            val newCustomerId = editCustomerId.text.toString().trim()
            val newQuantity = editQuantity.text.toString().trim()
            val newTotalPrice = editTotalPrice.text.toString().trim()

            if (newProductId.isNotEmpty() && newCustomerId.isNotEmpty() && newQuantity.isNotEmpty()) {
                transaction.productId = newProductId
                transaction.customerId = newCustomerId
                transaction.quantity = newQuantity
                transaction.totalPrice = newTotalPrice
                appViewModel.updateTransaction(transaction)
                dialog.dismiss()
            } else {
                if (newProductId.isEmpty()) editProductId.error = "ID Produk tidak boleh kosong"
                if (newCustomerId.isEmpty()) editCustomerId.error = "ID Customer tidak boleh kosong"
                if (newQuantity.isEmpty()) editQuantity.error = "Quantity tidak boleh kosong"
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}