package com.cioffi.debtcalculation.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Debt::class], version = 2, exportSchema = false)
abstract class RegistryDatabase : RoomDatabase() {

    abstract fun debtsDAO(): DebtsDAO

    companion object {
        @Volatile
        private var Instance: RegistryDatabase? = null

        fun getDataBase(context: Context): RegistryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RegistryDatabase::class.java, "debts_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}