package com.cioffi.debtcalculation.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "debts")
data class Debt(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateFormat: String = LocalDate.now().toString(),
    val description: String,
    val debtSign : String,
    val amount : Double
)
