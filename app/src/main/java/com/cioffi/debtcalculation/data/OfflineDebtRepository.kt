package com.cioffi.debtcalculation.data

import kotlinx.coroutines.flow.Flow

class OfflineDebtRepository(private val debtsDAO: DebtsDAO) :DebtsRepository{

    override fun getAllDebtsStream(): Flow<List<Debt>> = debtsDAO.getAllDebt()

    override fun getDebtStream(id: Int): Flow<Debt?> = debtsDAO.getDebt(id)

    override suspend fun insertDebt(Debt: Debt) = debtsDAO.insert(Debt)

    override suspend fun deleteDebt(Debt: Debt) = debtsDAO.delete(Debt)

    override suspend fun updateDebt(Debt: Debt) = debtsDAO.update(Debt)

}