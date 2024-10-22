    package com.example.project

    import android.content.Context
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.EditText
    import android.widget.ImageButton
    import android.widget.TextView
    import androidx.appcompat.app.AlertDialog
    import androidx.recyclerview.widget.RecyclerView
    import com.barleyan.managementoko.MainActivityProduct
    import com.barleyan.managementoko.MainActivityTransaction
    import com.barleyan.managementoko.R

    class TransactionAdapter(private var transactionList: MutableList<Transaction>) :
        RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

        // ViewHolder class for product items
        inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val productID: TextView = view.findViewById(R.id.tvProductId)
            val customerID: TextView = view.findViewById(R.id.tvCustomerId)
            val quantity: TextView = view.findViewById(R.id.tvquantity)
            val TotalPrice: TextView = view.findViewById(R.id.tvTotalPrice)
            val btnDeleteTransaction: ImageButton = view.findViewById(R.id.btnDeleteTransaction)
            val btnEditTransaction: ImageButton = view.findViewById(R.id.btnEditTransaction)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction, parent, false)
            return TransactionViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
            val transaction = transactionList[position]
            holder.productID.text = transaction.productId
            holder.customerID.text = transaction.customerId
            holder.quantity.text = transaction.quantity
            holder.TotalPrice.text = transaction.totalPrice

            // Setup delete button functionality
            holder.btnDeleteTransaction.setOnClickListener {
                showDeleteConfirmationDialog(holder.itemView.context, transaction, position)
            }

            // Setup edit button functionality
            holder.btnEditTransaction.setOnClickListener {
                showEditTransactionDialog(holder.itemView.context, transaction, position)
            }
        }

        override fun getItemCount(): Int = transactionList.size

        fun updateTransactions(newTransaction: List<Transaction>) {
            transactionList.clear() // clear the existing list
            transactionList.addAll(newTransaction) // add all new products
            notifyDataSetChanged()
        }

        // Show delete confirmation dialog
        private fun showDeleteConfirmationDialog(context: Context, transaction: Transaction, position: Int) {
            AlertDialog.Builder(context)
                .setTitle("Hapus Transaksi")
                .setMessage("Anda yakin ingin menghapus ${transaction.customerId}?")
                .setPositiveButton("Ya") { _, _ ->
                    deleteTransaction(context, transaction, position)
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        // Show edit product dialog
        private fun showEditTransactionDialog(context: Context, transaction: Transaction, position: Int) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Edit Transaksi")

            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_add_transaction, null)
            builder.setView(dialogView)

            val editProdukID = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
            val editCustomerID = dialogView.findViewById<EditText>(R.id.etCustomerID)
            val editQuantity = dialogView.findViewById<EditText>(R.id.etquantityID)
            val editTotalPrice = dialogView.findViewById<EditText>(R.id.ettotalPriceID)

            // Set current product data in the EditText fields
            editProdukID.setText(transaction.productId)
            editCustomerID.setText(transaction.customerId)
            editQuantity.setText(transaction.quantity)
            editTotalPrice.setText(transaction.totalPrice)

            // Set dialog actions
            builder.setPositiveButton("Simpan") { _, _ ->
                val newProduk = editProdukID.text.toString()
                val newCustomer = editCustomerID.text.toString()
                val newQuantity = editQuantity.text.toString()
                val newTotalPrice = editTotalPrice.text.toString()
                updateTransaction(context, transaction, position, newProduk, newCustomer, newQuantity, newTotalPrice)
            }
            builder.setNegativeButton("Batal", null)

            // Show the dialog
            builder.show()
        }

        // Update product details
        private fun updateTransaction(context: Context, transaction: Transaction, position: Int, newProduk: String, newCustomer: String, newQuantity: String, newtotalPrice: String) {
            // Update product data in ViewModel
            transaction.productId = newProduk
            transaction.customerId = newCustomer
            transaction.quantity = newQuantity
            transaction.totalPrice = newtotalPrice
            (context as MainActivityProduct).appViewModel.updateTransaction(transaction)

            // Notify adapter of the change
            notifyItemChanged(position)
        }

        // Delete product and notify adapter
        private fun deleteTransaction(context: Context, transaction: Transaction, position: Int) {
            // Call ViewModel function to delete product
            (context as MainActivityTransaction).appViewModel.deleteTransaction(transaction)

            // Remove item from list and notify adapter of changes
            transactionList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, transactionList.size)
        }
    }
