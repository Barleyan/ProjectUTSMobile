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
import com.example.project.Customer

class CustomerAdapter(private var customerList: MutableList<Customer>) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    // ViewHolder class
    inner class CustomerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val customerName: TextView = view.findViewById(R.id.tvCustomerName)
        val customerPhone: TextView = view.findViewById(R.id.tvCustomerPhone)
        val btnDeleteCustomer: ImageButton = view.findViewById(R.id.btnDeleteCustomer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
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
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    fun updateCustomers(newCustomers: List<Customer>) {
        this.customerList.clear() // clear the existing list
        this.customerList.addAll(newCustomers) // add all new customers
        notifyDataSetChanged()
    }

    // Function to show delete confirmation dialog
    private fun showDeleteConfirmationDialog(context: Context, customer: Customer, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Pelanggan")
            .setMessage("Anda yakin ingin menghapus ${customer.name}?")
            .setPositiveButton("Ya") { dialog, which ->
                deleteCustomer(context, customer, position)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // Function to delete customer
    private fun deleteCustomer(context: Context, customer: Customer, position: Int) {
        // Panggil fungsi dari ViewModel untuk menghapus customer
        (context as MainActivity).appViewModel.deleteCustomer(customer)

        // Menghapus item dari daftar dan memberitahu adapter tentang perubahan
        customerList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, customerList.size)
    }
}
