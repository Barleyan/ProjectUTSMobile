package com.barleyan.managementoko

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.Customer
import com.barleyan.managementoko.R

class CustomerAdapter(private var customerList: List<Customer>) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = customerList[position]
        holder.nameTextView.text = customer.name
        holder.phoneTextView.text = customer.phoneNumber
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    fun updateCustomers(newCustomers: List<Customer>) {
        customerList = newCustomers
        notifyDataSetChanged()
    }

    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvCustomerName)
        val phoneTextView: TextView = itemView.findViewById(R.id.tvCustomerPhone)
    }

}
