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
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var etforgotmobile : EditText
    lateinit var etforgotemail : EditText
    lateinit var btnforgotnext : Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        sharedPreferences = getSharedPreferences((getString(R.string.preference_filename)),
            Context.MODE_PRIVATE)

        etforgotmobile = findViewById(R.id.etForgotMobile)
        etforgotemail = findViewById(R.id.etForgotEmail)
        btnforgotnext = findViewById(R.id.btnForgotNext)

        val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
        val url ="http://13.235.250.119/v2/forgot_password/fetch_result"
        val params = JSONObject()

        btnforgotnext.setOnClickListener {

            params.put("mobile_number",etforgotmobile.text)
            params.put("email",etforgotemail.text)

            if(etforgotemail.text.toString().trim().length == 0 || etforgotmobile.text.toString().trim().length == 0){

                Toast.makeText(this@ForgotPasswordActivity,"please fill all the details",Toast.LENGTH_SHORT).show()


            }else {
                if (ConnectionManager().checkConnectivity(this@ForgotPasswordActivity)) {
                    val jsonObjectRequest = object : JsonObjectRequest(
                        Request.Method.POST, url, params, Response.Listener {

                            val jsnobj = it.getJSONObject("data")
                            val success = jsnobj.getBoolean("success")
                            if(success) {
                                val intentt =
                                    Intent(this@ForgotPasswordActivity, OtpActivity::class.java)
                                //intentt.putExtra("Mobile_Number", etForgotMobile.text)
                                sharedPreferences.edit().putString("MOBILE_NUMBER",etforgotmobile.text.toString()).apply()
                                startActivity(intentt)
                                finish()
                            }
                        },
                        Response.ErrorListener {

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

                    val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Settings") { text, Listner -> //
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit App") { text, Listner -> //
                        ActivityCompat.finishAffinity(this@ForgotPasswordActivity) //helps to exit app from any point
                    }
                    dialog.create()
                    dialog.show()
                }
            }
        }

    }
}
