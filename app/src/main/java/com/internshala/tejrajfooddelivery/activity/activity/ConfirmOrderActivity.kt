package com.internshala.tejrajfooddelivery.activity.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.room.Room
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.database.RestaurantDatabase

class ConfirmOrderActivity : AppCompatActivity() {

    lateinit var btnplaceorder : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)

        btnplaceorder =findViewById(R.id.btnPlaceOrder)

        btnplaceorder.setOnClickListener {
            Delteall(this@ConfirmOrderActivity,1).execute().get()
            val intent = Intent(this@ConfirmOrderActivity,MainActivity::class.java)
            startActivity(intent)
            finish()

        }


    }

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
