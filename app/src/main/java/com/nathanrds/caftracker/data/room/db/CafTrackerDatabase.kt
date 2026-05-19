package com.nathanrds.caftracker.data.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nathanrds.caftracker.data.room.converters.UnitTypeConverter
import com.nathanrds.caftracker.data.room.dao.IntakeDao
import com.nathanrds.caftracker.data.room.dao.ProductDao
import com.nathanrds.caftracker.data.room.entity.IntakeEntity
import com.nathanrds.caftracker.data.room.entity.ProductEntity

@Database(
    entities = [ProductEntity::class, IntakeEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(UnitTypeConverter::class)
abstract class CafTrackerDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun intakeDao(): IntakeDao
}