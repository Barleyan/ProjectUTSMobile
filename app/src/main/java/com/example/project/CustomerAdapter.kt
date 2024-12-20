import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.R
import com.example.project.Customer

// Adapter untuk RecyclerView yang digunakan untuk menampilkan daftar pelanggan
class CustomerAdapter(
    private val onEdit: (Customer) -> Unit, // Callback untuk aksi edit
    private val onDelete: (Customer) -> Unit // Callback untuk aksi hapus
) : ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

    // ViewHolder untuk merepresentasikan tampilan item pelanggan
    inner class CustomerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val customerName: TextView = view.findViewById(R.id.tvCustomerName) // Nama pelanggan
        val customerPhone: TextView = view.findViewById(R.id.tvCustomerPhone) // Nomor telepon pelanggan
        val btnDeleteCustomer: ImageButton = view.findViewById(R.id.btnDeleteCustomer) // Tombol hapus pelanggan
        val btnEditCustomer: ImageButton = view.findViewById(R.id.btnEditCustomer) // Tombol edit pelanggan
    }

    // Membuat ViewHolder baru saat RecyclerView memerlukan elemen baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer, parent, false) // Menghubungkan layout item_customer
        return CustomerViewHolder(itemView)
    }

    // Menghubungkan data pelanggan dengan tampilan item
    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = getItem(position) // Mendapatkan item pelanggan berdasarkan posisi
        holder.customerName.text = customer.name // Menampilkan nama pelanggan
        holder.customerPhone.text = customer.phoneNumber // Menampilkan nomor telepon pelanggan

        // Menambahkan aksi untuk tombol hapus
        holder.btnDeleteCustomer.setOnClickListener {
            onDelete(customer)
        }

        // Menambahkan aksi untuk tombol edit
        holder.btnEditCustomer.setOnClickListener {
            onEdit(customer)
        }
    }

    // DiffUtil untuk membandingkan item dalam daftar
    class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
        // Membandingkan apakah item lama dan item baru memiliki ID yang sama
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.id == newItem.id // Perbandingan berdasarkan ID unik
        }

        // Membandingkan apakah isi item lama dan item baru sama persis
        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }
    }
}