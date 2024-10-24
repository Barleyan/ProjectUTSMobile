package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.MainActivity
import com.barleyan.managementoko.R
import com.google.android.material.textfield.TextInputEditText

class CustomerAdapter(private var customerList: MutableList<Customer>) :
    RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    // ViewHolder class
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
        val customer = customerList[position]
        holder.customerName.text = customer.name
        holder.customerPhone.text = customer.phoneNumber

        // Setup delete button functionality
        holder.btnDeleteCustomer.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, customer, position)
        }

        // Setup edit button functionality
        holder.btnEditCustomer.setOnClickListener {
            showEditCustomerDialog(holder.itemView.context, customer, position)
        }
    }

    override fun getItemCount(): Int = customerList.size

    fun updateCustomers(newCustomers: List<Customer>) {
        customerList.clear() // Clear the existing list
        customerList.addAll(newCustomers) // Add all new customers
        notifyDataSetChanged()
    }

    // Show delete confirmation dialog
    private fun showDeleteConfirmationDialog(context: Context, customer: Customer, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Pelanggan")
            .setMessage("Anda yakin ingin menghapus ${customer.name}?")
            .setPositiveButton("Ya") { _, _ -> deleteCustomer(context, customer, position) }
            .setNegativeButton("Batal", null)
            .show()
    }

    // Show edit customer dialog
    private fun showEditCustomerDialog(context: Context, customer: Customer, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Pelanggan")

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_customer, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<TextInputEditText>(R.id.etName)
        val editPhone = dialogView.findViewById<TextInputEditText>(R.id.etPhone)

        // Set current customer data in the EditText fields
        editName.setText(customer.name)
        editPhone.setText(customer.phoneNumber)

        // Set dialog actions
        builder.setPositiveButton("Simpan") { dialog, _ ->
            val newName = editName.text.toString().trim()
            val newPhone = editPhone.text.toString().trim()
            if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                updateCustomer(context, customer, position, newName, newPhone)
                dialog.dismiss()
            } else {
                if (newName.isEmpty()) editName.error = "Nama tidak boleh kosong"
                if (newPhone.isEmpty()) editPhone.error = "Nomor telepon tidak boleh kosong"
            }
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }

        // Show the dialog
        builder.show()
    }

    // Update customer details
    private fun updateCustomer(context: Context, customer: Customer, position: Int, newName: String, newPhone: String) {
        // Update customer data in ViewModel
        customer.name = newName
        customer.phoneNumber = newPhone
        (context as MainActivity).appViewModel.updateCustomer(customer)

        // Notify adapter of the change
        notifyItemChanged(position)
    }

    // Delete customer and notify adapter
    private fun deleteCustomer(context: Context, customer: Customer, position: Int) {
        // Call ViewModel function to delete customer
        (context as MainActivity).appViewModel.deleteCustomer(customer)

        // Remove item from list and notify adapter of changes
        customerList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, customerList.size)
    }
}
