package com.internshala.tejrajfooddelivery.activity.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.adapter.DashboardRecyclerAdapter
import com.internshala.tejrajfooddelivery.activity.adapter.MenuCardRecyclerAdapter
import com.internshala.tejrajfooddelivery.activity.database.RestaurantDatabase
import com.internshala.tejrajfooddelivery.activity.database.RestaurantEntity
import com.internshala.tejrajfooddelivery.activity.model.MenuCard
import com.internshala.tejrajfooddelivery.activity.model.Restaurants
import com.internshala.tejrajfooddelivery.activity.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException

class MenuCardActivity : AppCompatActivity() {

    lateinit var recyclerMenu: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: MenuCardRecyclerAdapter
    lateinit var btnviewcart : Button
     var restaurant_id:String? = "100"
     var hotelname: String? = "Hotel"
    lateinit var toolbar: Toolbar



    val itemlist = arrayListOf<MenuCard>(
        /*MenuCard("chicken tikka","Rs 350","1"),
        MenuCard("chicken handi","Rs 550","2"),
        MenuCard("dal tadka","Rs 150","3"),
        MenuCard("Roti","Rs 20","4")*/
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_card)

        recyclerMenu = findViewById(R.id.MenuRecycler)
        layoutManager = LinearLayoutManager(this@MenuCardActivity)
        btnviewcart = findViewById(R.id.btnviewcart)

        restaurant_id=intent.getStringExtra("hotelid")
        hotelname=intent.getStringExtra("hotelname")

        btnviewcart.setOnClickListener {
            if (Delteall(this@MenuCardActivity, 2).execute().get()) {
                val dialog = AlertDialog.Builder(this@MenuCardActivity)
                dialog.setTitle(" Hey ")
                dialog.setMessage("Your cart is empty ")
                dialog.setPositiveButton("Ok") { text, Listner -> //

                }

                dialog.create()
                dialog.show()
            } else {
                val intent = Intent(this@MenuCardActivity, CartActivity::class.java)
                intent.putExtra("hotellname", hotelname)
                intent.putExtra("Restaurant_id",restaurant_id)
                startActivity(intent)
            }
        }

        toolbar = findViewById(R.id.MenuToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title=hotelname




        val queue = Volley.newRequestQueue(this@MenuCardActivity)

        val url = " http://13.235.250.119/v2/restaurants/fetch_result/" + restaurant_id

        if(ConnectionManager().checkConnectivity(this@MenuCardActivity)){
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                try {
                    val a = it.getJSONObject("data")
                    val success = a.getBoolean("success")
                    if (success) {
                        val data = a.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val hoteljsonobj = data.getJSONObject(i)
                            val hotelobj = MenuCard(
                                hoteljsonobj.getString("name"),
                                hoteljsonobj.getString("cost_for_one"),
                                hoteljsonobj.getString("id")
                                //(i+1).toString()
                            )
                            itemlist.add(hotelobj)
                            recyclerAdapter = MenuCardRecyclerAdapter(this@MenuCardActivity,itemlist)
                            recyclerMenu.adapter = recyclerAdapter
                            recyclerMenu.layoutManager = layoutManager

                        }
                    } else {
                        Toast.makeText(this@MenuCardActivity, " Error occured", Toast.LENGTH_SHORT)
                            .show()
                    }

                }catch (e: JSONException){
                    Toast.makeText(this@MenuCardActivity, "some unexpexted Error occured", Toast.LENGTH_SHORT)
                        .show()
                }
            },Response.ErrorListener {

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["content-type"] = "application/json"
                    headers["token"] = "ca8c4dbef672d6"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

            }else{

            val dialog = AlertDialog.Builder(this@MenuCardActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, Listner -> //
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit App") { text, Listner -> //
                ActivityCompat.finishAffinity(this@MenuCardActivity) //helps to exit app from any point
            }
            dialog.create()
            dialog.show()
        }
        }

    override fun onBackPressed() {
        val a=Delteall(this@MenuCardActivity,2).execute().get()
       if(!a) {
           val dialog = AlertDialog.Builder(this@MenuCardActivity)
           dialog.setTitle("Alert")
           dialog.setMessage("Your cart will get empty !! ")
           dialog.setPositiveButton("Ok") { text, Listner -> //

               Delteall(this@MenuCardActivity, 1).execute().get()
               super.onBackPressed()
           }
           dialog.setNegativeButton("Cancel") { text, Listner -> //

           }
           dialog.create()
           dialog.show()
       }else{
           super.onBackPressed()
       }
    }

    //


    class Delteall(val context: Context,val mode:Int) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val db= Room.databaseBuilder(context, RestaurantDatabase::class.java,"restaurant_db").build()
            //return db.restaurantDao().deleteallitems()
            when(mode){
            1 -> {
                db.restaurantDao().deleteallitems()
            db.close()
            return true
            }
                2 ->{
                   val book = db.restaurantDao().checkifempty()
                    db.close()
                    if(book==0) {
                        return true
                    }else{
                        return false
                    }
                }
            }
         return true
        }
    }


    }

