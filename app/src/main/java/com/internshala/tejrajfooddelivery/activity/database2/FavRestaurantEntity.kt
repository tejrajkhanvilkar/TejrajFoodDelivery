package com.internshala.tejrajfooddelivery.activity.database2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favrestaurant")
data class FavRestaurantEntity (
    @PrimaryKey val id : String,
    @ColumnInfo(name="hotel_name") val hotelname :String,
    @ColumnInfo(name="hotel_rating") val hotelrating :String,
    @ColumnInfo(name="hotel_price") val hotelprice :String,
    @ColumnInfo(name="hotel_img") val hotelimg :String
)
