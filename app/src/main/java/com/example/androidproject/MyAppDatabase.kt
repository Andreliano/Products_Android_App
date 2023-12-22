package com.example.androidproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidproject.todo.data.Item
import com.example.androidproject.todo.data.local.ItemDao

@Database(entities = arrayOf(Item::class), version=1)
@TypeConverters(Converters::class)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: MyAppDatabase? = null

        fun getDatabase(context: Context): MyAppDatabase {
            context.deleteDatabase("app_database")
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MyAppDatabase::class.java,
                    "app_database"
                )
                    .build()
                //instance.clearAllTables()
                INSTANCE = instance
                instance
            }
        }
    }
}
