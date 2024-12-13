package com.barleyan.managementoko

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
import com.barleyan.managementoko.R
import com.barleyan.managementoko.adapters.CustomerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.AlertDialog

class CustomerFragment : Fragment() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        setupRecyclerView(recyclerView)

        // Initialize ViewModel
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Observe customer data from ViewModel
        appViewModel.allCustomers.observe(viewLifecycleOwner) { customers ->
            customers?.let {
                customerAdapter.submitList(it) // Update the adapter's list
            }
        }

        // Setup FloatingActionButton for adding new customers
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddCustomerDialog()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        // Use a GridLayoutManager with 2 columns
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager

        // Initialize the adapter and pass the context correctly
        customerAdapter = CustomerAdapter(
            requireContext(), // Pass the context here
            onDelete = { customer ->
                appViewModel.deleteCustomer(customer)
                Toast.makeText(requireContext(), "${customer.name} telah dihapus", Toast.LENGTH_SHORT).show()
            },
            onEdit = { customer ->
                showEditCustomerDialog(customer)
            }
        )
        recyclerView.adapter = customerAdapter
    }

    private fun showAddCustomerDialog() {
        // Inflate the custom dialog view
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)

        // Build the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Tambah Client")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ -> }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()

        // Validate input before adding a customer
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = customerNameInput.text.toString().trim()
            val phoneNumber = customerPhoneInput.text.toString().trim()

            if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                val newCustomer = Customer(name = name, phoneNumber = phoneNumber)
                appViewModel.insertCustomer(newCustomer)
                dialog.dismiss()
            } else {
                // Show error messages
                if (name.isEmpty()) customerNameInput.error = "Nama tidak boleh kosong"
                if (phoneNumber.isEmpty()) customerPhoneInput.error = "Nomor telepon tidak boleh kosong"
            }
        }
    }

    private fun showEditCustomerDialog(customer: Customer) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Pelanggan")

        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_add_customer, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<EditText>(R.id.etName)
        val editPhone = dialogView.findViewById<EditText>(R.id.etPhone)

        // Pre-fill the fields with current customer data
        editName.setText(customer.name)
        editPhone.setText(customer.phoneNumber)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val newName = editName.text.toString().trim()
            val newPhone = editPhone.text.toString().trim()

            if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                customer.name = newName
                customer.phoneNumber = newPhone
                appViewModel.updateCustomer(customer)
                dialog.dismiss()
            } else {
                if (newName.isEmpty()) editName.error = "Nama tidak boleh kosong"
                if (newPhone.isEmpty()) editPhone.error = "Nomor telepon tidak boleh kosong"
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}