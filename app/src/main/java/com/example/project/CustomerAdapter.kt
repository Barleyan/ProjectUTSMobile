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
import com.barleyan.managementoko.MainActivity
import com.barleyan.managementoko.R
import com.example.project.Customer
import com.google.android.material.textfield.TextInputEditText

class CustomerAdapter : ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

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

        holder.btnDeleteCustomer.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, customer, position)
        }

        holder.btnEditCustomer.setOnClickListener {
            showEditCustomerDialog(holder.itemView.context, customer, position)
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, customer: Customer, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Pelanggan")
            .setMessage("Anda yakin ingin menghapus ${customer.name}?")
            .setPositiveButton("Ya") { _, _ -> deleteCustomer(context, customer, position) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditCustomerDialog(context: Context, customer: Customer, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Pelanggan")

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_customer, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<TextInputEditText>(R.id.etName)
        val editPhone = dialogView.findViewById<TextInputEditText>(R.id.etPhone)

        editName.setText(customer.name)
        editPhone.setText(customer.phoneNumber)

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

        builder.show()
    }

    private fun updateCustomer(context: Context, customer: Customer, position: Int, newName: String, newPhone: String) {
        customer.name = newName
        customer.phoneNumber = newPhone
        (context as MainActivity).appViewModel.updateCustomer(customer)

        notifyItemChanged(position)
    }

    private fun deleteCustomer(context: Context, customer: Customer, position: Int) {
        (context as MainActivity).appViewModel.deleteCustomer(customer)

        submitList(currentList.toMutableList().apply { removeAt(position) })
    }

    // Custom DiffUtil Callback
    class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.id == newItem.id // Use a unique identifier for comparison
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }
    }
}
