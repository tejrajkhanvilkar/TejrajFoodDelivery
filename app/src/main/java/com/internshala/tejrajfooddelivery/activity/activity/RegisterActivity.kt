package com.internshala.tejrajfooddelivery.activity.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    lateinit var register_name :EditText
    lateinit var register_email:EditText
    lateinit var register_mobile :EditText
    lateinit var register_addres :EditText
    lateinit var register_password :EditText
    lateinit var register_passwordconfirm :EditText
    lateinit var register_registerbtn :Button
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferences=getSharedPreferences(getString(R.string.preference_filename), Context.MODE_PRIVATE)

        val intent=Intent(this@RegisterActivity,MainActivity::class.java)

        register_name = findViewById(R.id.rregister_name)
        register_email = findViewById(R.id.rregister_email)
        register_mobile = findViewById(R.id.rregister_mobile)
        register_addres = findViewById(R.id.rregister_address)
        register_password = findViewById(R.id.rregister_password)
        //register_passwordconfirm = findViewById(R.id.rregister_passwordconfirm)
        register_registerbtn = findViewById(R.id.register_registerbtn)

        val queue = Volley.newRequestQueue(this@RegisterActivity)

        val url = "http://13.235.250.119/v2/register/fetch_result"

        val getParams = JSONObject()

        /*getParams.put("name","rajtej")
        getParams.put("mobile_number","8007777777")
        getParams.put("password","fuckthis")
        getParams.put("address","pune")
        getParams.put("email","rajtej@gmail.com")*/

        register_registerbtn.setOnClickListener {


            getParams.put("name",rregister_name.text)
            getParams.put("mobile_number",rregister_mobile.text)
            getParams.put("password",rregister_password.text)
            getParams.put("address",rregister_address.text)
            getParams.put("email",rregister_email.text)

            if (ConnectionManager().checkConnectivity(this@RegisterActivity)) {
                val jsonObjectRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, getParams, Response.Listener {

                        try {
                            val a = it.getJSONObject("data")
                            val success = a.getBoolean("success")
                           // val ermsg = a.getString("errorMessage")
                            if(success) {
                                val b = a.getJSONObject("data")
                                val email = b.getString("email")
                                val name = b.getString("name")
                                val mobile_number = b.getString("mobile_number")
                                val address = b.getString("address")
                                val userid = b.getString("user_id")

                                sharedPreferences.edit().putString("Email",email).apply()
                                  sharedPreferences.edit().putString("Name",name).apply()
                                  sharedPreferences.edit().putString("Mobile_number",mobile_number).apply()
                                   sharedPreferences.edit().putString("Address",address).apply()
                                sharedPreferences.edit().putBoolean("LogedIn",true).apply()
                                sharedPreferences.edit().putString("Userid",userid).apply()

                               /* val email_a = sharedPreferences.getString("Email","tejraj@gmail.com")
                                val name_a = sharedPreferences.getString("Name","tejraj")
                                val mobile_number_a = sharedPreferences.getString("Mobile_number","807777482")
                                val address_a = sharedPreferences.getString("Address","pune")*/

                              /*  val dialog = AlertDialog.Builder(this@RegisterActivity)
                                dialog.setTitle("Message")
                                dialog.setMessage("1. ${email_a} , 2. ${name_a} , 3. ${mobile_number_a} , 4. ${address_a} ")
                                dialog.setPositiveButton("Ok") { text, Listner -> //
                                }
                                dialog.setNegativeButton("Cancel") { text, Listner -> //
                                }
                                dialog.create()
                                dialog.show()*/




                                startActivity(intent)
                                finish()

                            }
                            else{
                                    val ermsg = a.getString("errorMessage")
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "ugh : ${ermsg}",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }



                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "some unexpexted Error occured",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@RegisterActivity,
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

                val dialog = AlertDialog.Builder(this@RegisterActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Settings") { text, Listner -> //
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit App") { text, Listner -> //
                    ActivityCompat.finishAffinity(this@RegisterActivity) //helps to exit app from any point
                }
                dialog.create()
                dialog.show()
            }
        }




    }
   /*override fun getParams():Map<String,String>{
        val params = HashMap<String,String>()
        params["name"]=register_name.toString()
       return params

    }*/
}
