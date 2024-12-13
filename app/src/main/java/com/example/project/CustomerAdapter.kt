package com.barleyan.managementoko.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.Customer
import com.barleyan.managementoko.R
import com.google.android.material.textfield.TextInputEditText

class CustomerAdapter(
    private val context: Context,
    private val onDelete: (Customer) -> Unit,
    private val onEdit: (Customer) -> Unit
) : ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

    inner class CustomerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val customerName: TextView = view.findViewById(R.id.tvCustomerName)
        val customerPhone: TextView = view.findViewById(R.id.tvCustomerPhone)
        val btnDeleteCustomer: ImageButton = view.findViewById(R.id.btnDeleteCustomer)
        val btnEditCustomer: ImageButton = view.findViewById(R.id.btnEditCustomer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer, parent, false)
        return CustomerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = getItem(position)
        holder.customerName.text = customer.name
        holder.customerPhone.text = customer.phoneNumber

        // Handling delete action
        holder.btnDeleteCustomer.setOnClickListener {
            showDeleteConfirmationDialog(customer)
        }

        // Handling edit action
        holder.btnEditCustomer.setOnClickListener {
            showEditCustomerDialog(customer)
        }
    }

    private fun showDeleteConfirmationDialog(customer: Customer) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Pelanggan")
            .setMessage("Anda yakin ingin menghapus ${customer.name}?")
            .setPositiveButton("Ya") { _, _ -> onDelete(customer) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditCustomerDialog(customer: Customer) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Pelanggan")

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_customer, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<TextInputEditText>(R.id.etName)
        val editPhone = dialogView.findViewById<TextInputEditText>(R.id.etPhone)

        // Pre-fill the fields with current customer data
        editName.setText(customer.name)
        editPhone.setText(customer.phoneNumber)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val newName = editName.text.toString().trim()
            val newPhone = editPhone.text.toString().trim()

            if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                // Update the customer and notify adapter
                val updatedCustomer = customer.copy(name = newName, phoneNumber = newPhone)
                onEdit(updatedCustomer)
                dialog.dismiss()
            } else {
                if (newName.isEmpty()) editName.error = "Nama tidak boleh kosong"
                if (newPhone.isEmpty()) editPhone.error = "Nomor telepon tidak boleh kosong"
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    // Custom DiffUtil Callback for comparing customer items
    class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.id == newItem.id // Use a unique identifier (ID) for comparison
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem // Check if the contents are the same
        }
    }
}