package com.internshala.tejrajfooddelivery.activity.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.database.RestaurantDatabase
import com.internshala.tejrajfooddelivery.activity.database.RestaurantEntity
import com.internshala.tejrajfooddelivery.activity.model.MenuCard
import com.internshala.tejrajfooddelivery.activity.model.Restaurants


class MenuCardRecyclerAdapter(val context: Context,val itemlist:ArrayList<MenuCard>):RecyclerView.Adapter<MenuCardRecyclerAdapter.MenuCardRecyclerViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuCardRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_hotelmenu_layout,parent,false)


        return MenuCardRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
      return itemlist.size
    }

    override fun onBindViewHolder(holder: MenuCardRecyclerViewHolder, position: Int) {
        val hotel=itemlist[position]
        holder.txtDishName.text= hotel.dishname
        holder.txtDishPrice.text= hotel.dishprice
        holder.txtDishNumber.text= hotel.dishnumber
        var bookEntity = RestaurantEntity(hotel.dishname,hotel.dishprice,hotel.dishnumber)
        holder.txtDishNumber.text= (position+1).toString()

        holder.btnadddish.setOnClickListener {
            //Toast.makeText(context,"dish added to cart",Toast.LENGTH_SHORT).show()




            if(!DBAsynctask(context,bookEntity,1).execute().get()) {
                val async = DBAsynctask(context, bookEntity, 2).execute()
                val result = async.get()

                // val result = true
                if (result) {
                    Toast.makeText(
                        context,
                        "dish added to Cart",
                        Toast.LENGTH_SHORT
                    ).show()

                    holder.btnadddish.text = "Remove"
                    val favcolor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                    holder.btnadddish.setBackgroundColor(favcolor)

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
                val async = DBAsynctask(context,bookEntity,3).execute()
                val result = async.get()

                if(result){
                    Toast.makeText(
                        context,
                        "dish removed from cart",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.btnadddish.text="Add "
                    val nofavclr = ContextCompat.getColor(context,R.color.colorPrimary)
                    holder.btnadddish.setBackgroundColor(nofavclr)

                }else{
                    Toast.makeText(
                        context,
                        "some error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }





        }
    }

    class MenuCardRecyclerViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtDishName : TextView = view.findViewById(R.id.dishname)
        val txtDishPrice : TextView = view.findViewById(R.id.dishprice)
        val txtDishNumber : TextView = view.findViewById(R.id.dishnumber)
        val btnadddish : Button = view.findViewById(R.id.btnadddish)

    }

    class DBAsynctask(val context: Context,val restaurantEntity: RestaurantEntity,val mode:Int) : AsyncTask<Void, Void, Boolean>(){

        /*
                                                                                      // mode 1 -> check if the book i fav or not
        mode 2-> add to cart
        mode 3-> remove from cart
         */
        val db = Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurant_db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){
                1 -> {
                    // check if bok is fav or not
                   val book:RestaurantEntity? = db.restaurantDao().getbookbyid(restaurantEntity.id.toString())
                    db.close()
                    return book!=null
                }
                2 -> {
                    // add to cart
                    db.restaurantDao().insertitem(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    //remove from cart
                    db.restaurantDao().deleteitem(restaurantEntity)
                    db.close()
                    return true
                }
            }

            return false
        }
    }
}