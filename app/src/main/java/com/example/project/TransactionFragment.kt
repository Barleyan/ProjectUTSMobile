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

// Fragment untuk mengelola transaksi di aplikasi
class TransactionFragment : Fragment() {

    private lateinit var appViewModel: AppViewModel // ViewModel untuk menangani data transaksi
    private lateinit var transactionAdapter: TransactionAdapter // Adapter untuk RecyclerView

    // Fungsi untuk membuat tampilan fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_transaction
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    // Fungsi untuk menyiapkan tampilan setelah fragment dibuat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView untuk menampilkan daftar transaksi
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        setupRecyclerView(recyclerView)

        // Inisialisasi ViewModel
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Mengamati data transaksi dari ViewModel
        appViewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            transactions?.let {
                transactionAdapter.submitList(it) // Memperbarui daftar transaksi pada adapter
            }
        }

        // Menyiapkan FloatingActionButton untuk menambah transaksi baru
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddTransactionDialog() // Menampilkan dialog untuk menambah transaksi baru
        }
    }

    // Fungsi untuk menyiapkan RecyclerView dengan GridLayoutManager dan adapter
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        // Menggunakan GridLayoutManager dengan 2 kolom
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager

        // Inisialisasi adapter dengan konteks dan handler untuk tombol delete dan edit
        transactionAdapter = TransactionAdapter(
            requireContext(), // Mengirimkan konteks ke adapter
            onDelete = { transaction ->
                appViewModel.deleteTransaction(transaction) // Menghapus transaksi
                Toast.makeText(requireContext(), "Transaksi ${transaction.productId} telah dihapus", Toast.LENGTH_SHORT).show()
            },
            onEdit = { transaction ->
                showEditTransactionDialog(transaction) // Menampilkan dialog untuk mengedit transaksi
            }
        )
        recyclerView.adapter = transactionAdapter // Menetapkan adapter pada RecyclerView
    }

    // Fungsi untuk menampilkan dialog untuk menambah transaksi baru
    private fun showAddTransactionDialog() {
        // Menggunakan layout dialog custom
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_transaction, null)
        val productIDInput = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
        val customerIDInput = dialogView.findViewById<EditText>(R.id.etCustomerID)
        val quantityIDInput = dialogView.findViewById<EditText>(R.id.etQuantityID)
        val totalPriceIDInput = dialogView.findViewById<EditText>(R.id.etTotalPriceID)

        // Membangun dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Tambah Transaksi")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ -> }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()

        // Validasi input sebelum menambah transaksi
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val productId = productIDInput.text.toString().trim()
            val customerId = customerIDInput.text.toString().trim()
            val quantity = quantityIDInput.text.toString().trim()
            val totalPrice = totalPriceIDInput.text.toString().trim()

            // Jika input tidak kosong, buat transaksi baru
            if (productId.isNotEmpty() && customerId.isNotEmpty() && quantity.isNotEmpty()) {
                val newTransaction = Transaction(
                    productId = productId, customerId = customerId, quantity = quantity, totalPrice = totalPrice)
                appViewModel.insertTransaction(newTransaction) // Menambahkan transaksi ke ViewModel
                dialog.dismiss() // Menutup dialog
            } else {
                // Menampilkan error jika input kosong
                if (productId.isEmpty()) productIDInput.error = "ID Produk tidak boleh kosong"
                if (customerId.isEmpty()) customerIDInput.error = "ID Customer tidak boleh kosong"
                if (quantity.isEmpty()) quantityIDInput.error = "Quantity tidak boleh kosong"
            }
        }
    }

    // Fungsi untuk menampilkan dialog untuk mengedit transaksi yang ada
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

        // Memasukkan data transaksi yang ada ke dalam field
        editProductId.setText(transaction.productId)
        editCustomerId.setText(transaction.customerId)
        editQuantity.setText(transaction.quantity)
        editTotalPrice.setText(transaction.totalPrice)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val newProductId = editProductId.text.toString().trim()
            val newCustomerId = editCustomerId.text.toString().trim()
            val newQuantity = editQuantity.text.toString().trim()
            val newTotalPrice = editTotalPrice.text.toString().trim()

            // Validasi input sebelum memperbarui transaksi
            if (newProductId.isNotEmpty() && newCustomerId.isNotEmpty() && newQuantity.isNotEmpty()) {
                transaction.productId = newProductId
                transaction.customerId = newCustomerId
                transaction.quantity = newQuantity
                transaction.totalPrice = newTotalPrice
                appViewModel.updateTransaction(transaction) // Memperbarui transaksi di ViewModel
                dialog.dismiss() // Menutup dialog
            } else {
                // Menampilkan error jika input kosong
                if (newProductId.isEmpty()) editProductId.error = "ID Produk tidak boleh kosong"
                if (newCustomerId.isEmpty()) editCustomerId.error = "ID Customer tidak boleh kosong"
                if (newQuantity.isEmpty()) editQuantity.error = "Quantity tidak boleh kosong"
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show() // Menampilkan dialog
    }
}