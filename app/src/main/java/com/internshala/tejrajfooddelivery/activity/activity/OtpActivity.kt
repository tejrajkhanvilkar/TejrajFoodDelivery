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
import org.json.JSONObject

class OtpActivity : AppCompatActivity() {

    lateinit var otpenterotp : EditText
    lateinit var otpenterpassword : EditText
    lateinit var otpenterpasswordconfirm : EditText
    lateinit var otpsubmitbtn :Button
    lateinit var a : String
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

       // val mobilenumber = intent.getStringExtra("Mobile_Number")
        sharedPreferences = getSharedPreferences((getString(R.string.preference_filename)),
            Context.MODE_PRIVATE)
        val mobilenumber = sharedPreferences.getString("MOBILE_NUMBER",null)
        //a=mobilenumber
        otpenterotp = findViewById(R.id.otpEnterotp)
        otpenterpassword = findViewById(R.id.otpEnterpassword)
        otpenterpasswordconfirm = findViewById(R.id.otpEnterpasswordconfirm)
        otpsubmitbtn = findViewById(R.id.otpSubmitbtn)

        Toast.makeText(this@OtpActivity,"${mobilenumber} ",Toast.LENGTH_SHORT).show()

        val queue = Volley.newRequestQueue(this@OtpActivity)
        val url ="http://13.235.250.119/v2/reset_password/fetch_result"
        val params = JSONObject()

        otpsubmitbtn.setOnClickListener {

            params.put("mobile_number",mobilenumber )
            params.put("password",otpenterpassword.text)
            params.put("otp",otpenterotp.text)

            if (ConnectionManager().checkConnectivity(this@OtpActivity)) {
                val jsonObjectRequest = object : JsonObjectRequest(
                    Request.Method.POST, url, params, Response.Listener {

                        val a = it.getJSONObject("data")
                        val success = a.getBoolean("success")
                        //val msg = a.getString("successMessage")
                        if(success){
                            val dialog = AlertDialog.Builder(this@OtpActivity)
                            dialog.setTitle("Message")
                            dialog.setMessage(" Password has successfully changed ")
                            dialog.setPositiveButton("Ok") { text, Listner -> //

                                val intent = Intent(this@OtpActivity,LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            //dialog.setNegativeButton("") { text, Listner -> //
                              //  ActivityCompat.finishAffinity(this@OtpActivity) //helps to exit app from any point
                            //}
                            dialog.create()
                            dialog.show()
                        }else{
                            Toast.makeText(this@OtpActivity,"Error : New Password not set ",Toast.LENGTH_LONG).show()
                        }

                    },
                    Response.ErrorListener {
                        Toast.makeText(this@OtpActivity,"Volley error occured ",Toast.LENGTH_LONG).show()

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

                val dialog = AlertDialog.Builder(this@OtpActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Settings") { text, Listner -> //
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit App") { text, Listner -> //
                    ActivityCompat.finishAffinity(this@OtpActivity) //helps to exit app from any point
                }
                dialog.create()
                dialog.show()
            }



        }



    }
}
