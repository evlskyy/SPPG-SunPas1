package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.DailyMenuEntity
import com.example.data.model.ExtractionRecordEntity
import com.example.data.model.HaccpCheckEntity
import com.example.data.model.KitchenTaskEntity

@Database(
    entities = [
        KitchenTaskEntity::class,
        DailyMenuEntity::class,
        HaccpCheckEntity::class,
        ExtractionRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun kitchenDao(): KitchenDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dapur_mbg.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
