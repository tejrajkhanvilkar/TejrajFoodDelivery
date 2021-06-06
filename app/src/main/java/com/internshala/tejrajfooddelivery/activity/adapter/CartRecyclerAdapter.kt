package com.internshala.tejrajfooddelivery.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.database.RestaurantEntity
import com.internshala.tejrajfooddelivery.activity.model.MenuCard

class CartRecyclerAdapter(val context:Context,val itemlist : List<RestaurantEntity>):RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_cart_layout,parent,false)

        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemlist.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val hotel=itemlist[position]
        holder.txtDishname.text= hotel.dishname
        holder.txtDishprice.text= hotel.costforone
    }

    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtDishname : TextView= view.findViewById(R.id.cartDishname)
        val txtDishprice : TextView = view.findViewById(R.id.cartsingledishprice)
    }
}