package com.cioffi.debtcalculation.data
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Debt] from a given data source.
 */
interface DebtsRepository {
    /**
     * Retrieve all the Debts from the the given data source.
     */
    fun getAllDebtsStream(): Flow<List<Debt>>

    /**
     * Retrieve an Debt from the given data source that matches with the [id].
     */
    fun getDebtStream(id: Int): Flow<Debt?>

    /**
     * Insert Debt in the data source
     */
    suspend fun insertDebt(Debt: Debt)

    /**
     * Delete Debt from the data source
     */
    suspend fun deleteDebt(Debt: Debt)

    /**
     * Update Debt in the data source
     */
    suspend fun updateDebt(Debt: Debt)
}
