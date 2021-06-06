package com.internshala.tejrajfooddelivery.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.model.MenuCard
import com.internshala.tejrajfooddelivery.activity.model.Orderhistory
import com.internshala.tejrajfooddelivery.activity.model.SubHeadingData2
import kotlinx.android.synthetic.main.single_row_cart_layout.view.*

class CartRecyclerAdaptertwo(val itemlist : ArrayList<SubHeadingData2>):RecyclerView.Adapter<CartRecyclerAdaptertwo.CartViewHolderTwo>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolderTwo {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_cart_layout,parent,false)
        return  CartViewHolderTwo(view)
    }

    override fun getItemCount(): Int {
        return  itemlist.size
    }

    override fun onBindViewHolder(holder: CartViewHolderTwo, position: Int) {
        val hotel  = itemlist[position]
        holder.txtdishname.text = hotel.dishname
        holder.txtdishprice.text = hotel.dishprice
    }

    class CartViewHolderTwo(view: View):RecyclerView.ViewHolder(view){
        val txtdishname :TextView=view.findViewById(R.id.cartDishname)
        val txtdishprice : TextView = view.findViewById(R.id.cartsingledishprice)

    }
}