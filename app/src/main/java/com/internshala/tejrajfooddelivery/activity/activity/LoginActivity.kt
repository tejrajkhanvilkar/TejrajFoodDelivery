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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.fragment.MyProfileFragment
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var mobilenumber:EditText
    lateinit var password : EditText
    lateinit var btnlogin: Button
    lateinit var forgotpassword : TextView
    lateinit var registeruser : TextView
    val defaultnumber="8007777482"
    val defaultpassword = "tejraj"
    lateinit var inputpassword:String
    lateinit var inputmobilenumber :String
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences=getSharedPreferences(getString(R.string.preference_filename), Context.MODE_PRIVATE)
        val isloggedin = sharedPreferences.getBoolean("LogedIn",false)
        setContentView(R.layout.activity_login)

        val intent =Intent(this@LoginActivity, MainActivity::class.java)





        if(isloggedin){
            startActivity(intent)
            finish()
        }


        mobilenumber = findViewById(R.id.login_mobilenumber)
        password = findViewById(R.id.login_password)
        btnlogin = findViewById(R.id.login_loginbutton)
        forgotpassword = findViewById(R.id.login_forgotpassword)
        registeruser = findViewById(R.id.login_Register)




        val queue = Volley.newRequestQueue(this@LoginActivity)

        val url = "http://13.235.250.119/v2/login/fetch_result/"
        val params = JSONObject()

        btnlogin.setOnClickListener {









            //inputpassword = password.text.toString()
            //inputmobilenumber = mobilenumber.text.toString()

            params.put("mobile_number",login_mobilenumber.text)
                       params.put("password",login_password.text)

                       val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, params, Response.Listener {

                           val a = it.getJSONObject("data")
                           val success = a.getBoolean("success")
                           //val ermsg = a.getBoolean("errorMessage")


                           if(success){
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


                               startActivity(intent)
                               //Toast.makeText(this@LoginActivity,"hii ${c}",Toast.LENGTH_SHORT).show()
                           }else{
                               val ermsg = a.getString("errorMessage")
                               val dialog = AlertDialog.Builder(this@LoginActivity)
                               dialog.setTitle("Oops")
                               dialog.setMessage(" ${ermsg} ")
                               dialog.setPositiveButton("Ok") { text, Listner -> //

                               }
                              // dialog.setNegativeButton("Exit App") { text, Listner -> //

                               //}
                               dialog.create()
                               dialog.show()
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











            // inputpassword = password.text.toString()
            //inputmobilenumber = mobilenumber.text.toString()

              /*if(defaultpassword==inputpassword && defaultnumber==inputmobilenumber){
                  val intent =Intent(this@LoginActivity,
                      MainActivity::class.java)
                  startActivity(intent)
              }else{
                  Toast.makeText(this@LoginActivity,"invalid input",Toast.LENGTH_SHORT).show()
              }*/


        }


        forgotpassword.setOnClickListener {
            val intent = Intent(this@LoginActivity,
                ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        registeruser.setOnClickListener {
            val intent = Intent(this@LoginActivity,
                RegisterActivity::class.java)
            startActivity(intent)
        }

        /*val bundle = Bundle()
        bundle.putString("monile_number",inputmobilenumber)
        MyProfileFragment().arguments = bundle*/


    }
}
