package com.barleyan.managementoko

import CustomerAdapter
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
import com.example.project.AppViewModel
import com.example.project.Customer
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        customerAdapter = CustomerAdapter(
            onEdit = { customer ->
                showEditCustomerDialog(customer)
            },
            onDelete = { customer ->
                deleteCustomer(customer)
            }
        )
        recyclerView.adapter = customerAdapter

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        appViewModel.allCustomers.observe(viewLifecycleOwner) { customers ->
            customers?.let { customerAdapter.submitList(it) }
        }

        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddCustomerDialog()
        }
    }

    private fun showAddCustomerDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Tambah Client")
            .setView(dialogView)
            .setPositiveButton("Tambah", null)
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = customerNameInput.text.toString().trim()
            val phoneNumber = customerPhoneInput.text.toString().trim()

            if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                val newCustomer = Customer(name = name, phoneNumber = phoneNumber)
                appViewModel.insertCustomer(newCustomer)
                dialog.dismiss()
            } else {
                if (name.isEmpty()) customerNameInput.error = "Nama tidak boleh kosong"
                if (phoneNumber.isEmpty()) customerPhoneInput.error = "Nomor telepon tidak boleh kosong"
            }
        }
    }

    private fun showEditCustomerDialog(customer: Customer) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)

        customerNameInput.setText(customer.name)
        customerPhoneInput.setText(customer.phoneNumber)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Client")
            .setView(dialogView)
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val updatedName = customerNameInput.text.toString().trim()
            val updatedPhoneNumber = customerPhoneInput.text.toString().trim()

            if (updatedName.isNotEmpty() && updatedPhoneNumber.isNotEmpty()) {
                val updatedCustomer = customer.copy(name = updatedName, phoneNumber = updatedPhoneNumber)
                appViewModel.updateCustomer(updatedCustomer)
                dialog.dismiss()
            } else {
                if (updatedName.isEmpty()) customerNameInput.error = "Nama tidak boleh kosong"
                if (updatedPhoneNumber.isEmpty()) customerPhoneInput.error = "Nomor telepon tidak boleh kosong"
            }
        }
    }

    private fun deleteCustomer(customer: Customer) {
        appViewModel.deleteCustomer(customer)
        Toast.makeText(requireContext(), "${customer.name} telah dihapus", Toast.LENGTH_SHORT).show()
    }
}
