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

    private lateinit var appViewModel: AppViewModel // ViewModel untuk mengelola data pelanggan
    private lateinit var customerAdapter: CustomerAdapter // Adapter untuk RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_customer sebagai tampilan fragment ini
        return inflater.inflate(R.layout.fragment_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView dan menetapkan tata letak grid dengan 2 kolom
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Inisialisasi adapter dengan fungsi edit dan delete
        customerAdapter = CustomerAdapter(
            onEdit = { customer ->
                showEditCustomerDialog(customer) // Menampilkan dialog edit pelanggan
            },
            onDelete = { customer ->
                deleteCustomer(customer) // Menghapus pelanggan
            }
        )
        recyclerView.adapter = customerAdapter

        // Inisialisasi ViewModel
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Mengamati perubahan data pelanggan dan memperbarui daftar di adapter
        appViewModel.allCustomers.observe(viewLifecycleOwner) { customers ->
            customers?.let { customerAdapter.submitList(it) }
        }

        // Menangani tombol tambah pelanggan
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddCustomerDialog() // Menampilkan dialog tambah pelanggan
        }
    }

    // Menampilkan dialog untuk menambahkan pelanggan baru
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
                appViewModel.insertCustomer(newCustomer) // Menyisipkan pelanggan baru
                dialog.dismiss()
            } else {
                // Validasi input
                if (name.isEmpty()) customerNameInput.error = "Nama tidak boleh kosong"
                if (phoneNumber.isEmpty()) customerPhoneInput.error = "Nomor telepon tidak boleh kosong"
            }
        }
    }

    // Menampilkan dialog untuk mengedit data pelanggan
    private fun showEditCustomerDialog(customer: Customer) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)

        // Mengisi input dengan data pelanggan saat ini
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
                // Membuat objek pelanggan dengan data yang diperbarui
                val updatedCustomer = customer.copy(name = updatedName, phoneNumber = updatedPhoneNumber)
                appViewModel.updateCustomer(updatedCustomer) // Memperbarui data pelanggan
                dialog.dismiss()
            } else {
                // Validasi input
                if (updatedName.isEmpty()) customerNameInput.error = "Nama tidak boleh kosong"
                if (updatedPhoneNumber.isEmpty()) customerPhoneInput.error = "Nomor telepon tidak boleh kosong"
            }
        }
    }

    // Menghapus pelanggan
    private fun deleteCustomer(customer: Customer) {
        appViewModel.deleteCustomer(customer) // Menghapus pelanggan dari database
        Toast.makeText(requireContext(), "${customer.name} telah dihapus", Toast.LENGTH_SHORT).show()
    }
}