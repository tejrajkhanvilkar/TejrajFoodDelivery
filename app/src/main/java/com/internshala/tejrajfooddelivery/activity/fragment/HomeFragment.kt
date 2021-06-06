package com.internshala.tejrajfooddelivery.activity.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.adapter.DashboardRecyclerAdapter
import com.internshala.tejrajfooddelivery.activity.model.Restaurants
import com.internshala.tejrajfooddelivery.activity.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter


    /*val itemlist = arrayListOf<Restaurants>(Restaurants("0","Nimantran","5.0","599/person"),
        Restaurants("1","kolhapuri katta","4.5","450/person"),
        Restaurants("2","Navratna","4.0","300/person"),
        Restaurants("3","Tirange","3.9","499/person"),
        Restaurants("4","Westin","5.0","799/person"),
        Restaurants("5","Monafood","4.3","200/person"))*/

    val itemlist_two = arrayListOf<Restaurants>()
        var ratingcomparator = Comparator<Restaurants>{ Restaurant1, Restaurant2 ->
        if (Restaurant1.hotelRating.compareTo(Restaurant2.hotelRating, true) == 0) {

            Restaurant1.hotelName.compareTo(Restaurant2.hotelName, true)

        } else {
            Restaurant1.hotelRating.compareTo(Restaurant2.hotelRating, true)
        }
    }

    var ratingcomparator1 = Comparator<Restaurants>{ Restaurant1, Restaurant2 ->
        if (Restaurant1.hotelPrice.compareTo(Restaurant2.hotelPrice, true) == 0) {

            Restaurant1.hotelName.compareTo(Restaurant2.hotelName, true)

        } else {
            Restaurant1.hotelPrice.compareTo(Restaurant2.hotelPrice, true)
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val  view=inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(activity)




        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if(ConnectionManager().checkConnectivity(activity as Context)){

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                //println("response:$it")
                try {
                    val a = it.getJSONObject("data")
                    val success = a.getBoolean("success")
                    if (success) {
                        val data = a.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val hoteljsonobj = data.getJSONObject(i)
                            val hotelobj = Restaurants(
                                hoteljsonobj.getString("id"),
                                hoteljsonobj.getString("name"),
                                hoteljsonobj.getString("rating"),
                                hoteljsonobj.getString("cost_for_one"),
                                hoteljsonobj.getString("image_url")
                            )
                            itemlist_two.add(hotelobj)

                            recyclerAdapter =
                                DashboardRecyclerAdapter(activity as Context, itemlist_two)

                            recyclerDashboard.adapter = recyclerAdapter

                            recyclerDashboard.layoutManager = layoutManager
                        }
                    } else {
                        Toast.makeText(activity as Context, " Error occured", Toast.LENGTH_SHORT)
                            .show()
                    }

                }catch (e: JSONException){
                    Toast.makeText(activity as Context, "some unexpexted Error occured", Toast.LENGTH_SHORT)
                        .show()
                }
            },Response.ErrorListener {
                Toast.makeText(activity as Context, "Volley Error occured", Toast.LENGTH_SHORT).show()
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
            val dialog =AlertDialog.Builder(activity as Context)
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


        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater?.inflate(R.menu.menu_dashboard,menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {

            val id=item?.itemId
            if(id==R.id.action_sort){
                Collections.sort(itemlist_two,ratingcomparator)//this will sort in ascending order but we need descending order
                itemlist_two.reverse()
            }

            if(id==R.id.action_sortbyprice1){
                Collections.sort(itemlist_two,ratingcomparator1)//this will sort in ascending order but we need descending order
                itemlist_two.reverse()
            }

            if(id==R.id.action_sortbyprice2){
                Collections.sort(itemlist_two,ratingcomparator1)//this will sort in ascending order but we need descending order
               // itemlist_two.reverse()
            }


            recyclerAdapter.notifyDataSetChanged()

            return super.onOptionsItemSelected(item)
        }

}
