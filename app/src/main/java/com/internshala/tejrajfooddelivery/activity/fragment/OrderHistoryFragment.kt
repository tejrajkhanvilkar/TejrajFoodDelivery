package com.internshala.tejrajfooddelivery.activity.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.adapter.CartRecyclerAdaptertwo
import com.internshala.tejrajfooddelivery.activity.adapter.OrderHistoryAdapter
import com.internshala.tejrajfooddelivery.activity.model.MenuCard
import com.internshala.tejrajfooddelivery.activity.model.Orderhistory
import com.internshala.tejrajfooddelivery.activity.model.SubHeadingData2
import com.internshala.tejrajfooddelivery.activity.util.ConnectionManager
import org.json.JSONException

/**
 * A simple [Fragment] subclass.
 */
class OrderHistoryFragment : Fragment() {

    lateinit var recyclerMenu1: RecyclerView
    lateinit var recyclerMenu2: RecyclerView
    lateinit var layoutManager1: RecyclerView.LayoutManager
    lateinit var layoutManager2: RecyclerView.LayoutManager
    lateinit var recyclerAdapter1: OrderHistoryAdapter
    lateinit var recyclerAdapter2: CartRecyclerAdaptertwo
    lateinit var sharedPreferences: SharedPreferences
    var userid:String? = "100"

    val itemlist = arrayListOf<Orderhistory>( )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        sharedPreferences = (activity as FragmentActivity).getSharedPreferences((getString(R.string.preference_filename)),Context.MODE_PRIVATE)

         userid = sharedPreferences.getString("Userid",null)

        recyclerMenu1 = view.findViewById(R.id.recyclerorderhistroy1)

        layoutManager1 = LinearLayoutManager(activity)



        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/orders/fetch_result/" + userid

        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null, Response.Listener {

                    try {
                        val a = it.getJSONObject("data")
                        val success = a.getBoolean("success")
                        if (success) {
                            val data = a.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val hoteljsonobj = data.getJSONObject(i)


                                val itemlist2 = arrayListOf<SubHeadingData2>( )

                                val hoteljsonobj2 = hoteljsonobj.getJSONArray("food_items")

                                for (i in 0 until hoteljsonobj2.length()) {

                                    val hoteljsonobj3 = hoteljsonobj2.getJSONObject(i)
                                    val hotelobj2 = SubHeadingData2(

                                        hoteljsonobj3.getString("name"),
                                        hoteljsonobj3.getString("cost"),
                                        hoteljsonobj3.getString("food_item_id")
                                    )

                                    itemlist2.add(hotelobj2)

                                }


                                val hotelobj = Orderhistory(
                                    hoteljsonobj.getString("restaurant_name"),
                                    hoteljsonobj.getString("order_placed_at"),
                                    itemlist2
                                )

                                itemlist.add(hotelobj)

                                recyclerAdapter1 =
                                    OrderHistoryAdapter(activity as Context, itemlist)
                                recyclerMenu1.adapter = recyclerAdapter1
                                recyclerMenu1.layoutManager = layoutManager1


                            }


                        } else {
                            Toast.makeText(activity as Context, " Error occured", Toast.LENGTH_SHORT)
                                .show()
                            Toast.makeText(activity as Context, " ${userid} ", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }catch (e: JSONException){
                        Toast.makeText(activity as Context, "some unexpexted Error occured", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                Response.ErrorListener {

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

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, Listner -> //
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit App") { text, Listner -> //
                ActivityCompat.finishAffinity(activity as Activity) //helps to exit app from any point
            }
            dialog.create()
            dialog.show()
        }


        return view
    }

}
