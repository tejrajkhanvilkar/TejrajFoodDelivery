package com.internshala.tejrajfooddelivery.activity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.activity.MenuCardActivity
import com.internshala.tejrajfooddelivery.activity.database2.FavRestaurantEntity
import com.squareup.picasso.Picasso

class FavHotelRecyclerAdapter(val context: Context,val itemlist:List<FavRestaurantEntity>):RecyclerView.Adapter<FavHotelRecyclerAdapter.FavHotelViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_favhotel_layout,parent,false)

        return FavHotelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: FavHotelViewHolder, position: Int) {
        val hotel = itemlist[position]
        holder.txtfavhotelname.text = hotel.hotelname
        holder.txtfavhotelprice.text = hotel.hotelprice
        holder.txtfavhotelrating.text = hotel.hotelrating
        Picasso.get().load(hotel.hotelimg).error(R.drawable.ic_person2).into(holder.imgfavhotelimage)

        //
        holder.imgheart.setImageResource(R.drawable.ic_heart_b)

        holder.contentlayoutt.setOnClickListener {
            //Toast.makeText(context,"fav restaurant clicked",Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MenuCardActivity::class.java)
            intent.putExtra("hotelid",hotel.id)
            intent.putExtra("hotelname",hotel.hotelname)

            context.startActivity(intent)
        }
    }

    class FavHotelViewHolder(view: View):RecyclerView.ViewHolder(view){

        val imgfavhotelimage :ImageView= view.findViewById(R.id.imgFavHotelImage)
        val txtfavhotelname : TextView = view.findViewById(R.id.txtFavHotelName)
        val txtfavhotelprice : TextView  = view.findViewById(R.id.txtFavHotelPrice)
        val txtfavhotelrating : TextView = view.findViewById(R.id.txtFavHotelRating)
        val imgheart :ImageView= view.findViewById(R.id.imgheart)
        val contentlayoutt : RelativeLayout = view.findViewById(R.id.contetlayoutt)
    }
}