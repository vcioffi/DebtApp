package com.cioffi.debtcalculation.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cioffi.debtcalculation.DebtApplication
import com.cioffi.debtcalculation.ui.debt.DebtEntryViewModel
import com.cioffi.debtcalculation.ui.home.HomeViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                debtApplication().container.debtRepository
            )
        }
        // Initializer for HomeViewModel
        initializer {
            DebtEntryViewModel(
                debtApplication().container.debtRepository
            )
        }
    }
}

fun CreationExtras.debtApplication(): DebtApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as DebtApplication)
