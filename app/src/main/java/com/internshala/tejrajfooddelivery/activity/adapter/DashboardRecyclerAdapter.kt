package com.internshala.tejrajfooddelivery.activity.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.activity.MenuCardActivity
import com.internshala.tejrajfooddelivery.activity.database2.FavRestaurantDatabase
import com.internshala.tejrajfooddelivery.activity.database2.FavRestaurantEntity
import com.internshala.tejrajfooddelivery.activity.model.Restaurants
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context,val itemlist:ArrayList<Restaurants>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

//lateinit var sharedPreferences: SharedPreferences
  // var  sharedPreferences=(context).getSharedPreferences(getString(R.string.preference_filename), Context.MODE_PRIVATE)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_home_layout,parent,false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {

        return itemlist.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val hotel=itemlist[position]
        holder.txtHotelName.text= hotel.hotelName
        holder.txtHotelPrice.text= hotel.hotelPrice
        holder.txtHotelRating.text= hotel.hotelRating
        Picasso.get().load(hotel.hotelimg).error(R.drawable.ic_person2).into(holder.imgHotelImage)

        var bookEntity = FavRestaurantEntity(hotel.hotelId,hotel.hotelName,hotel.hotelRating,hotel.hotelPrice,hotel.hotelimg)

        val checkfav = DBAsynctask2(context,bookEntity,1).execute()
        val isFav=checkfav.get()
        if(isFav){
           /* btnfav.text="Remove From Favourite"
            val favclr = ContextCompat.getColor(applicationContext,R.color.colorfav)
            btnfav.setBackgroundColor(favclr)*/
            holder.imgheart.setImageResource(R.drawable.ic_heart_b)

        }else{
            /*btnfav.text="Add to Favourite"
            val nofavclr = ContextCompat.getColor(applicationContext,R.color.colorPrimaryDark)
            btnfav.setBackgroundColor(nofavclr)*/
            holder.imgheart.setImageResource(R.drawable.ic_heart_a)
        }

        holder.imgheart.setOnClickListener {
            //Toast.makeText(context,"heart got red",Toast.LENGTH_SHORT).show()
           // holder.imgheart.setImageResource(R.drawable.ic_heart_b)


            if(!DBAsynctask2(context,bookEntity,1).execute().get()) {
                val async = DBAsynctask2(context, bookEntity, 2).execute()
                val result = async.get()

                // val result = true
                if (result) {
                    Toast.makeText(
                        context,
                        "hotel added to Fav",
                        Toast.LENGTH_SHORT
                    ).show()

                    //holder.btnadddish.text = "Remove"
                    holder.imgheart.setImageResource(R.drawable.ic_heart_b)
                   // val favcolor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                    //holder.btnadddish.setBackgroundColor(favcolor)
                } else {
                    Toast.makeText(
                        context,
                        "some error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }else{
                /*Toast.makeText(
                    context,
                    "dish removed from Cart",
                    Toast.LENGTH_SHORT
                ).show()

                holder.btnadddish.text="Add"
                val favcolor = ContextCompat.getColor(context,R.color.colorPrimary)
                holder.btnadddish.setBackgroundColor(favcolor)*/
                val async = DBAsynctask2(context,bookEntity,3).execute()
                val result = async.get()

                if(result){
                    Toast.makeText(
                        context,
                        "hotel removed from fav",
                        Toast.LENGTH_SHORT
                    ).show()
                    //holder.btnadddish.text="Add "
                    holder.imgheart.setImageResource(R.drawable.ic_heart_a)
                    //val nofavclr = ContextCompat.getColor(context,R.color.colorPrimary)
                    //holder.btnadddish.setBackgroundColor(nofavclr)

                }else{
                    Toast.makeText(
                        context,
                        "some error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        holder.contentlayout.setOnClickListener {
            val intent = Intent(context,MenuCardActivity::class.java)
            intent.putExtra("hotelid",hotel.hotelId)
            intent.putExtra("hotelname",hotel.hotelName)

            context.startActivity(intent)
        }
    }

    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view){

        val txtHotelName : TextView = view.findViewById(R.id.txtFavHotelName)
        val txtHotelPrice : TextView = view.findViewById(R.id.txtHotelPrice)
        val txtHotelRating : TextView = view.findViewById(R.id.txtHotelRating)
        val imgHotelImage : ImageView = view.findViewById(R.id.imgFavHotelImage)
        val contentlayout : RelativeLayout = view.findViewById(R.id.contetlayout)
        val imgheart:ImageView=view.findViewById(R.id.imgheart)
    }

    class DBAsynctask2(val context: Context,val favrestaurantEntity: FavRestaurantEntity,val mode:Int) : AsyncTask<Void, Void, Boolean>(){

        /*
        mode 1 -> check if the hotel is fav or not
        mode 2-> add to fav
        mode 3-> remove from fav
         */
        //val db = Room.databaseBuilder(context,FavRestaurantDatabase::class.java,"favrestaurant_db").build()

        val db = Room.databaseBuilder(context,FavRestaurantDatabase::class.java,"favrestaurant_db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){
                1 -> {
                    // check if hotel is fav or not
                    val hotel:FavRestaurantEntity? = db.favRestaurantDao().gethotelbyid(favrestaurantEntity.id.toString())
                    db.close()
                    return hotel!=null
                }
                2 -> {
                    // add to cart
                    db.favRestaurantDao().inserthotel(favrestaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    //remove from cart
                    db.favRestaurantDao().deletehotel(favrestaurantEntity)
                    db.close()
                    return true
                }
            }

            return false
        }
    }
}