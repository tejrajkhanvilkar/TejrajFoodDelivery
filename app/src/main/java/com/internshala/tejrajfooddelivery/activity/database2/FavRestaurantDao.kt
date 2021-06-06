package com.internshala.tejrajfooddelivery.activity.database2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.internshala.tejrajfooddelivery.activity.database.RestaurantEntity

@Dao
interface FavRestaurantDao {

    @Insert
    fun inserthotel(favRestaurantEntity: FavRestaurantEntity)

    @Delete
    fun deletehotel(favRestaurantEntity: FavRestaurantEntity)

    @Query("SELECT * FROM favrestaurant")
    fun getallhotels():List<FavRestaurantEntity>

    @Query("SELECT * FROM favrestaurant WHERE id= :hotelId")
    fun gethotelbyid(hotelId:String): FavRestaurantEntity

    @Query("select count(*) from favrestaurant")
    fun checkifempty():Int
}