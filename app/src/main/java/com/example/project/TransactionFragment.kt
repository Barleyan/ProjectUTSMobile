package com.barleyan.managementoko.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.AppViewModel
import com.barleyan.managementoko.R
import com.barleyan.managementoko.Transaction
import com.example.project.TransactionAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TransactionFragment : Fragment() {

    // Correctly initialize the appViewModel using viewModels() delegate
    private val appViewModel: AppViewModel by viewModels()

    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        transactionAdapter = TransactionAdapter(
            onEdit = { transaction -> showEditTransactionDialog(transaction) },
            onDelete = { transaction -> deleteTransaction(transaction) }
        )
        recyclerView.adapter = transactionAdapter

        appViewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            transactions?.let {
                transactionAdapter.submitList(it)
            }
        }

        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddTransactionDialog()
        }
    }

    private fun showAddTransactionDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_transaction, null)
        val produkIDInput = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
        val customerIDInput = dialogView.findViewById<EditText>(R.id.etCustomerID)
        val quantityInput = dialogView.findViewById<EditText>(R.id.etQuantityID)
        val totalPriceInput = dialogView.findViewById<EditText>(R.id.etTotalPriceID)

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Transaksi")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ ->
                val produkID = produkIDInput.text.toString().trim()
                val customerID = customerIDInput.text.toString().trim()
                val quantity = quantityInput.text.toString().trim()
                val totalPrice = totalPriceInput.text.toString().trim()

                if (produkID.isEmpty() || customerID.isEmpty() || quantity.isEmpty() || totalPrice.isEmpty()) {
                    Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                } else {
                    val newTransaction = Transaction(
                        productId = produkID,
                        customerId = customerID,
                        quantity = quantity.toInt().toString(),
                        totalPrice = totalPrice.toDouble().toString(),
                        amount = 0.0 // Assuming amount calculation is done elsewhere
                    )
                    appViewModel.insertTransaction(newTransaction)
                    Toast.makeText(requireContext(), "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .create()
            .show()
    }

    private fun showEditTransactionDialog(transaction: Transaction) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_transaction, null)
        val produkIDInput = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
        val customerIDInput = dialogView.findViewById<EditText>(R.id.etCustomerID)
        val quantityInput = dialogView.findViewById<EditText>(R.id.etQuantityID)
        val totalPriceInput = dialogView.findViewById<EditText>(R.id.etTotalPriceID)

        // Prefill the dialog with existing transaction data
        produkIDInput.setText(transaction.productId)
        customerIDInput.setText(transaction.customerId)
        quantityInput.setText(transaction.quantity.toString())
        totalPriceInput.setText(transaction.totalPrice.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Transaksi")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val produkID = produkIDInput.text.toString().trim()
                val customerID = customerIDInput.text.toString().trim()
                val quantity = quantityInput.text.toString().trim()
                val totalPrice = totalPriceInput.text.toString().trim()

                if (produkID.isEmpty() || customerID.isEmpty() || quantity.isEmpty() || totalPrice.isEmpty()) {
                    Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedTransaction = transaction.copy(
                        productId = produkID,
                        customerId = customerID,
                        quantity = quantity.toInt().toString(),
                        totalPrice = totalPrice.toDouble().toString()
                    )
                    appViewModel.updateTransaction(updatedTransaction)
                    Toast.makeText(requireContext(), "Transaksi berhasil diperbarui", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .create()
            .show()
    }

    private fun deleteTransaction(transaction: Transaction) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Transaksi")
            .setMessage("Apakah Anda yakin ingin menghapus transaksi ini?")
            .setPositiveButton("Hapus") { _, _ ->
                appViewModel.deleteTransaction(transaction)
                Toast.makeText(requireContext(), "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .create()
            .show()
    }
}