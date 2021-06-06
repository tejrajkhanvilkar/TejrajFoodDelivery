package com.internshala.tejrajfooddelivery.activity.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

import com.internshala.tejrajfooddelivery.R

/**
 * A simple [Fragment] subclass.
 */
class MyProfileFragment : Fragment() {

    lateinit var txtprofilename : TextView
    lateinit var txtprofilephnno : TextView
    lateinit var txtprofileemail : TextView
    lateinit var txtprofileaddress : TextView
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)



        sharedPreferences = (activity as FragmentActivity).getSharedPreferences((getString(R.string.preference_filename)),Context.MODE_PRIVATE)

        txtprofilename = view.findViewById(R.id.txtProfilename)
        txtprofilephnno = view.findViewById(R.id.txtProfilephnno)
        txtprofileemail = view.findViewById(R.id.txtProfileemail)
        txtprofileaddress = view.findViewById(R.id.txtProfileaddress)




        txtprofilephnno.text = sharedPreferences.getString("Mobile_number",/*"8007777482"*/ null)
        txtprofilename.text = sharedPreferences.getString("Name",/*"tejraj"*/ null)
        txtprofileemail.text = sharedPreferences.getString("Email",/*"tejraj@gmail.com"*/ null)
        txtprofileaddress.text = sharedPreferences.getString("Address",/*"pune"*/ null)








        return view
    }

}
