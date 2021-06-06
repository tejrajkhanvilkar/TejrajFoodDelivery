package com.internshala.tejrajfooddelivery.activity.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menucard")
data class RestaurantEntity(

    @ColumnInfo(name="name") val dishname :String,
    @ColumnInfo(name="cost_for_one") val costforone:String,
    @PrimaryKey val id:String
)

/*"id": "50",
"name": "Vegetarian Chicken",
"cost_for_one": "340"*/

/*@Entity(tableName = "menucard")
data class FavRestaurantEntity(

    @ColumnInfo(name="name") val dishname :String,
    @ColumnInfo(name="cost_for_one") val costforone:String,
    @PrimaryKey val id:String
)*/