package com.internshala.tejrajfooddelivery.activity.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.internshala.tejrajfooddelivery.R
import com.internshala.tejrajfooddelivery.activity.fragment.*

class MainActivity : AppCompatActivity() {

    lateinit var coordinatorlayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var navigationView: NavigationView
    lateinit var frameLayout: FrameLayout
    lateinit var drawerLayout: DrawerLayout
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences((getString(R.string.preference_filename)), Context.MODE_PRIVATE)

        coordinatorlayout = findViewById(R.id.coordinatelayout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationview)
        frameLayout = findViewById(R.id.frame)
        drawerLayout = findViewById(R.id.drawerlayout)


        setupToolbar()
        openHome()


        val actionBarDrawerToggle= ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()





       // header_username.text=sharedPreferences.getString("Name",null)
        //header_userphnno.text = sharedPreferences.getString("Mobile_number",null)
        val convertView = LayoutInflater.from(this@MainActivity).inflate(R.layout.header_drawer, null)
        val userName: TextView = convertView.findViewById(R.id.header_username)
        val userPhone: TextView = convertView.findViewById(R.id.header_userphnno)
       // val appIcon: ImageView = convertView.findViewById(R.id.imgDrawerImage)
        userName.text = sharedPreferences.getString("Name", null)
        val phoneText = "+91-${sharedPreferences.getString("Mobile_number", null)}"
        userPhone.text = phoneText
        navigationView.addHeaderView(convertView)




        navigationView.setNavigationItemSelectedListener {


            when(it.itemId){
                R.id.menu_home -> {
                    Toast.makeText(this@MainActivity,"clicked home", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            HomeFragment()
                        )
                        .commit()

                    supportActionBar?.title="Home"
                    drawerLayout.closeDrawers()
                }
                R.id.menu_favrestaurants -> {
                    Toast.makeText(this@MainActivity,"clicked favourite", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouriteFragment()
                        )
                        .commit()

                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.menu_myprofile -> {
                    Toast.makeText(this@MainActivity,"clicked on myprofile", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            MyProfileFragment()
                        )
                        .commit()

                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.menu_orderhistory -> {
                    Toast.makeText(this@MainActivity,"clicked order history", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            OrderHistoryFragment()
                        )
                        .commit()

                    supportActionBar?.title="orderhistory"
                    drawerLayout.closeDrawers()
                }
                R.id.menu_faq -> {
                    Toast.makeText(this@MainActivity,"clicked on favourites", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FaqFragment()
                        )
                        .commit()

                    supportActionBar?.title="orderhistory"
                    drawerLayout.closeDrawers()
                }
                R.id.menu_logout -> {
                   // Toast.makeText(this@MainActivity,"yuo have succesfully logged out", Toast.LENGTH_SHORT).show()
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Message")
                    dialog.setMessage("Logout from your account ?")
                    dialog.setPositiveButton("Yes") { text, Listner -> //
                        //sharedPreferences.edit().clear().apply()



                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()
                        //finish()


                        //ActivityCompat.finishAffinity(this@MainActivity)
                        val intent = Intent(this@MainActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()


                    }
                    dialog.setNegativeButton("No") { text, Listner -> //

                    }
                    dialog.create()
                    dialog.show()



                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }


    }

    fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "The Book App"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }


    fun openHome(){
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title="Home"
        navigationView.setCheckedItem(R.id.menu_home)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is HomeFragment -> openHome()

            is HomeFragment -> { val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("Message")
                dialog.setMessage("Exit the app ?")
                dialog.setPositiveButton("Yes") { text, Listner -> //
                    ActivityCompat.finishAffinity(this@MainActivity)
                }
                dialog.setNegativeButton("No") { text, Listner -> //

                }
                dialog.create()
                dialog.show()





            }

            else-> super.onBackPressed()
        }
    }
}
