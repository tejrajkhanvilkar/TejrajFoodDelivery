package com.internshala.tejrajfooddelivery.activity.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {

    @Insert
    fun insertitem(restaurantEntity:RestaurantEntity)

    @Delete
    fun deleteitem(restaurantEntity:RestaurantEntity)

    @Query("select * from menucard")
    fun getallitems():List<RestaurantEntity>

    @Query("SELECT * FROM menucard WHERE id= :dishId")
    fun getbookbyid(dishId:String): RestaurantEntity

//
    @Query("delete from menucard")
    fun deleteallitems()

    @Query("select count(*) from menucard")
    fun checkifempty():Int

    @Query("select sum(cost_for_one) from menucard")
    fun additemprice():Int
}