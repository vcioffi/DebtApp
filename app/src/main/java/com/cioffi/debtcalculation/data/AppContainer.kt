package com.cioffi.debtcalculation.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val debtRepository: DebtsRepository
}


class AppDataContainer(private val context: Context) : AppContainer {
    override val debtRepository: DebtsRepository by lazy {
        OfflineDebtRepository(RegistryDatabase.getDataBase(context).debtsDAO())
    }
}