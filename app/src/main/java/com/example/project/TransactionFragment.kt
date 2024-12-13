package com.barleyan.managementoko.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.AppViewModel
import com.barleyan.managementoko.R
import com.barleyan.managementoko.Transaction
import com.barleyan.managementoko.TransactionAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TransactionFragment : Fragment() {

    lateinit var appViewModel: AppViewModel
    lateinit var transactionAdapter: TransactionAdapter

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

        transactionAdapter = TransactionAdapter()
        recyclerView.adapter = transactionAdapter

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

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
        val quantityIDInput = dialogView.findViewById<EditText>(R.id.etQuantityID)
        val totalPriceIDInput = dialogView.findViewById<EditText>(R.id.etTotalPriceID)

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Transaksi")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ ->
                val produkID = produkIDInput.text.toString().toInt()  // Convert to Int
                val customerID = customerIDInput.text.toString().toInt()  // Convert to Int
                val quantityID = quantityIDInput.text.toString().toInt()  // Convert to Int
                val totalPriceID = totalPriceIDInput.text.toString().toInt()  // Convert to Int

                if (produkID > 0 && customerID > 0) {  // Ensure IDs are positive
                    val transaction = Transaction(produkID, customerID, quantityID, totalPriceID)
                    appViewModel.insertTransaction(transaction)
                }
            }
            .setNegativeButton("Batal", null)
            .create()
            .show()
    }
}