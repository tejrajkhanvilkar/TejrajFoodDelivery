package com.internshala.tejrajfooddelivery.activity.model

data class Orderhistory(
    val hotelname : String,
    val orderdate : String,
    var subData: ArrayList<SubHeadingData2>
)

data class SubHeadingData2 (
    val dishname:String,
    val dishprice:String,
    val dishnumber:String
    )


