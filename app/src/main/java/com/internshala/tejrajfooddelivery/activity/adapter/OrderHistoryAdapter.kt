package com.internshala.tejrajfooddelivery.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.model.MenuCard
import com.internshala.tejrajfooddelivery.activity.model.Orderhistory
import kotlinx.android.synthetic.main.single_row_orderhistory_layout.view.*

class OrderHistoryAdapter(val context: Context,val itemlist : ArrayList<Orderhistory>):RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryHotelNameViewHolder>() {

   private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryHotelNameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_orderhistory_layout,parent,false)

        return OrderHistoryHotelNameViewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemlist.size
    }

    override fun onBindViewHolder(holder: OrderHistoryHotelNameViewHolder, position: Int) {
        holder.itemView.tag = position
        val hotel = itemlist[position]
        holder.txtHotelname.text = hotel.hotelname
        holder.txtDate.text = hotel.orderdate


        val childLayoutManager =
            LinearLayoutManager(holder.itemView.recyclerorderhistroy2.context, /*LinearLayout.VERTICAL*/RecyclerView.VERTICAL, false)

        holder.itemView.recyclerorderhistroy2.apply {
            layoutManager = childLayoutManager
            adapter = CartRecyclerAdaptertwo(hotel.subData)
            setRecycledViewPool(viewPool)
        }
    }


    class OrderHistoryHotelNameViewHolder(view: View):RecyclerView.ViewHolder(view){

        val txtHotelname : TextView = view.findViewById(R.id.hotelname)
        val txtDate : TextView = view.findViewById(R.id.orderdate)
    }

}