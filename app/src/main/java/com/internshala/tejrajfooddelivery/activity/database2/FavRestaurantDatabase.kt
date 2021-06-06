package com.internshala.tejrajfooddelivery.activity.database2

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase

@Database(entities = [FavRestaurantEntity::class],version = 1)
abstract  class FavRestaurantDatabase:RoomDatabase() {

    abstract fun favRestaurantDao(): FavRestaurantDao

}