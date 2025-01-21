package com.cioffi.debtcalculation

import android.app.Application
import com.cioffi.debtcalculation.data.AppContainer
import com.cioffi.debtcalculation.data.AppDataContainer

class DebtApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
