package com.internshala.tejrajfooddelivery.activity.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.internshala.tejrajfooddelivery.R

class LogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)


        Handler().postDelayed({
            val showlogo = Intent(this@LogoActivity,
                LoginActivity::class.java)
            startActivity(showlogo)
            finish()
        },2000)


    }
}
