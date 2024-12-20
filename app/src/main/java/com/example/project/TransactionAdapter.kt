package com.barleyan.managementoko

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project.Transaction

// Adapter untuk menampilkan data transaksi dalam RecyclerView
class TransactionAdapter(
    private val context: Context,
    private val onDelete: (Transaction) -> Unit, // Lambda untuk aksi hapus transaksi
    private val onEdit: (Transaction) -> Unit // Lambda untuk aksi edit transaksi
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    // ViewHolder untuk elemen transaksi dalam RecyclerView
    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Mendeklarasikan variabel untuk komponen UI yang ada pada item layout transaksi
        val productID: TextView = view.findViewById(R.id.tvProductId)
        val customerID: TextView = view.findViewById(R.id.tvCustomerId)
        val quantity: TextView = view.findViewById(R.id.tvQuantity)
        val totalPrice: TextView = view.findViewById(R.id.tvTotalPrice)
        val btnDeleteTransaction: ImageButton = view.findViewById(R.id.btnDeleteTransaction)
        val btnEditTransaction: ImageButton = view.findViewById(R.id.btnEditTransaction)
    }

    // Menginisialisasi ViewHolder dan menghubungkan layout item dengan RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        // Memuat layout untuk item transaksi
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView) // Mengembalikan ViewHolder untuk item transaksi
    }

    // Mengikat data transaksi ke dalam ViewHolder yang telah dibuat
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position) // Mengambil transaksi berdasarkan posisi
        // Menampilkan data transaksi di tampilan yang sesuai
        holder.productID.text = transaction.productId
        holder.customerID.text = transaction.customerId
        holder.quantity.text = transaction.quantity
        holder.totalPrice.text = transaction.totalPrice

        // Mengatur aksi ketika tombol hapus ditekan
        holder.btnDeleteTransaction.setOnClickListener {
            showDeleteConfirmationDialog(transaction) // Menampilkan dialog konfirmasi hapus
        }

        // Mengatur aksi ketika tombol edit ditekan
        holder.btnEditTransaction.setOnClickListener {
            showEditTransactionDialog(transaction) // Menampilkan dialog untuk mengedit transaksi
        }
    }

    // Menampilkan dialog konfirmasi sebelum menghapus transaksi
    private fun showDeleteConfirmationDialog(transaction: Transaction) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Transaksi")
            .setMessage("Anda yakin ingin menghapus transaksi?")
            .setPositiveButton("Ya") { _, _ -> onDelete(transaction) } // Menangani aksi hapus
            .setNegativeButton("Batal", null) // Menangani aksi batal
            .show()
    }

    // Menampilkan dialog untuk mengedit transaksi
    private fun showEditTransactionDialog(transaction: Transaction) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Transaksi")

        // Memuat layout untuk dialog edit transaksi
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_transaction, null)
        builder.setView(dialogView)

        // Mendapatkan referensi elemen-elemen dalam dialog
        val editProductId = dialogView.findViewById<TextView>(R.id.etProdukTransaction)
        val editCustomerId = dialogView.findViewById<TextView>(R.id.etCustomerID)
        val editQuantity = dialogView.findViewById<TextView>(R.id.etQuantityID)
        val editTotalPrice = dialogView.findViewById<TextView>(R.id.etTotalPriceID)

        // Mengisi kolom-kolom dialog dengan data transaksi yang ada
        editProductId.text = transaction.productId
        editCustomerId.text = transaction.customerId
        editQuantity.text = transaction.quantity
        editTotalPrice.text = transaction.totalPrice

        // Menangani aksi saat tombol Simpan diklik
        builder.setPositiveButton("Simpan") { dialog, _ ->
            // Membuat transaksi yang diperbarui dari input pengguna
            val updatedTransaction = transaction.copy(
                productId = editProductId.text.toString(),
                customerId = editCustomerId.text.toString(),
                quantity = editQuantity.text.toString(),
                totalPrice = editTotalPrice.text.toString()
            )
            onEdit(updatedTransaction) // Menyimpan perubahan transaksi
            dialog.dismiss() // Menutup dialog setelah perubahan disimpan
        }

        builder.setNegativeButton("Batal", null) // Menangani aksi batal
        builder.show() // Menampilkan dialog
    }

    // Callback DiffUtil untuk membandingkan item transaksi
    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        // Membandingkan item berdasarkan ID untuk memeriksa apakah mereka item yang sama
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        // Membandingkan konten item untuk memeriksa apakah mereka identik
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}