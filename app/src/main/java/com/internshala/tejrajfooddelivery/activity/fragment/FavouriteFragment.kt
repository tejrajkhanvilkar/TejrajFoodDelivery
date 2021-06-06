package com.internshala.tejrajfooddelivery.activity.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.adapter.FavHotelRecyclerAdapter
import com.internshala.tejrajfooddelivery.activity.database.RestaurantDatabase
import com.internshala.tejrajfooddelivery.activity.database2.FavRestaurantDatabase
import com.internshala.tejrajfooddelivery.activity.database2.FavRestaurantEntity

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavHotelRecyclerAdapter
    var dbbooklist = listOf<FavRestaurantEntity>()
    lateinit var txtnofav : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFavourite = view.findViewById(R.id.favRecyclerview)
        layoutManager = LinearLayoutManager(activity)
        txtnofav = view.findViewById(R.id.txtnofav)
        txtnofav.visibility = View.GONE

        dbbooklist = RetrieveFavourite(activity as Context).execute().get()

      if(Checkifempty(activity as Context).execute().get() ){
              txtnofav.visibility =View.VISIBLE
          }

        //}

        if(activity!=null){

            recyclerAdapter = FavHotelRecyclerAdapter(activity as Context,dbbooklist)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }



        return view
    }


    class RetrieveFavourite(val context: Context) : AsyncTask<Void, Void, List<FavRestaurantEntity>>() {
        override fun doInBackground(vararg params: Void?): List<FavRestaurantEntity> {
            val db= Room.databaseBuilder(context, FavRestaurantDatabase::class.java,"favrestaurant_db").build()

            return db.favRestaurantDao().getallhotels()
        }
    }

    //

    class Checkifempty(val context: Context) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val db= Room.databaseBuilder(context, FavRestaurantDatabase::class.java,"favrestaurant_db").build()

            var a = db.favRestaurantDao().checkifempty()
            if(a==0){
                return true
            }
            else{
                return false
            }

        }
    }

    }

