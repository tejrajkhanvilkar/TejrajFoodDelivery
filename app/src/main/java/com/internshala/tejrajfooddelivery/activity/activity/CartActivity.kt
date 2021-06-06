package com.internshala.tejrajfooddelivery.activity.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.adapter.CartRecyclerAdapter
import com.internshala.tejrajfooddelivery.activity.database.RestaurantDatabase
import com.internshala.tejrajfooddelivery.activity.database.RestaurantEntity
import com.internshala.tejrajfooddelivery.activity.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class CartActivity : AppCompatActivity() {

    lateinit var recyclerMenu: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var cartHotelname:TextView
    lateinit var btnsubmiorder :Button
    lateinit var sharedPreferences: SharedPreferences
     var userid : String? = null

    var dbbooklist = listOf<RestaurantEntity>()
    var amountofalldish : Int?=null
    var hotelname: String? = "Hotel"
    var restaurantid:String?="5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        sharedPreferences=getSharedPreferences(getString(R.string.preference_filename), Context.MODE_PRIVATE)
         userid = sharedPreferences.getString("Userid",null)

        hotelname=intent.getStringExtra("hotellname")
        restaurantid=intent.getStringExtra("Restaurant_id")
        toolbar = findViewById(R.id.CartToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Your Cart"


        cartHotelname = findViewById(R.id.cartHotelname)
        cartHotelname.text = hotelname



        recyclerMenu = findViewById(R.id.cartRecycler)
        layoutManager = LinearLayoutManager(this@CartActivity)

        dbbooklist = RetrieveFavourite(this@CartActivity).execute().get()// getting dishes which are added to cart

        amountofalldish = Amountofalldish(this@CartActivity).execute().get()//getting the sum of all dish prices

        btnsubmiorder = findViewById(R.id.btnPlaceorder)
        val a =amountofalldish.toString()
        btnsubmiorder.text =" Place Order (Total Rs ${a}) "


            if(this@CartActivity!=null){
            //   progreslayout.visibility = View.GONE
            recyclerAdapter = CartRecyclerAdapter(this@CartActivity,dbbooklist)
            recyclerMenu.adapter = recyclerAdapter
            recyclerMenu.layoutManager = layoutManager
        }


        val queue = Volley.newRequestQueue(this@CartActivity)

        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        //val demo =JSONObject()

        val food = JSONArray()


        for(i in 0 until  dbbooklist.size) {
            val value_a = dbbooklist[i]
            val gson = Gson()
            val fooditem = gson.toJson(value_a)
            val jobj = JSONObject(fooditem)
            val b = jobj.getString("id")

            val demo = JSONObject()
            demo.put("food_item_id", b.toString())
            //val fooditem2 = gson.toJson(demo)
            food.put(demo)
        }










        val params = JSONObject()
        params.put("user_id",userid.toString())
        params.put("restaurant_id",restaurantid)
        params.put("total_cost",a)
        params.put("food",food)




        btnsubmiorder.setOnClickListener {

            //Toast.makeText(this@CartActivity," ${b} ",Toast.LENGTH_LONG).show()
            if (ConnectionManager().checkConnectivity(this@CartActivity)) {
                val jsonObjectRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, params, Response.Listener {

                        try {
                            val a = it.getJSONObject("data")
                            val success = a.getBoolean("success")
                            // val ermsg = a.getString("errorMessage")
                            if(success) {
                                //Toast.makeText(this@CartActivity,"success value true",Toast.LENGTH_LONG).show()


                                val intent  = Intent(this@CartActivity,ConfirmOrderActivity::class.java)
                                startActivity(intent)
                                finish()


                            }
                            if(!success){
                                Toast.makeText(this@CartActivity,"success value false",Toast.LENGTH_LONG).show()
                            }



                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@CartActivity,
                                "some unexpexted Error occured",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@CartActivity,
                            "Volley Error occured",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["content-type"] = "application/json"
                        headers["token"] = "ca8c4dbef672d6"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

            } else {

                val dialog = AlertDialog.Builder(this@CartActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Settings") { text, Listner -> //
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit App") { text, Listner -> //
                    ActivityCompat.finishAffinity(this@CartActivity) //helps to exit app from any point
                }
                dialog.create()
                dialog.show()
            }



        }
















    }


    class RetrieveFavourite(val context: Context) : AsyncTask<Void, Void, List<RestaurantEntity>>() {
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val db= Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurant_db").build()
            return db.restaurantDao().getallitems()
        }
    }

    class Amountofalldish(val context: Context) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            val db= Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurant_db").build()
            return db.restaurantDao().additemprice()
        }
    }


}
