package com.cioffi.debtcalculation.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow



@Dao
interface DebtsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(debt :Debt)

    @Update
    suspend fun update(debt :Debt)

    @Delete
    suspend fun delete(debt :Debt)

    @Query("SELECT * from debts WHERE id = :id")
    fun getDebt(id :Int): Flow<Debt>

    @Query("SELECT * FROM debts ORDER BY dateFormat DESC")
    fun getAllDebt(): Flow<List<Debt>>
}