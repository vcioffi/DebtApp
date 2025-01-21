package com.cioffi.debtcalculation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cioffi.debtcalculation.data.Debt
import com.cioffi.debtcalculation.data.DebtsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat


class HomeViewModel(private val debtsRepository: DebtsRepository) : ViewModel() {

    val homeUiState : StateFlow<HomeUiState> =
        debtsRepository.getAllDebtsStream().map { HomeUiState(it,calcBalance(it)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteDebt(debt: Debt) {
        debtsRepository.deleteDebt(debt)
    }


    fun deleteUiDebtState(debt: Debt) {
        viewModelScope.launch {
            deleteDebt(debt)
        }
    }

}

fun calcBalance(debtList: List<Debt>) : String {
    var balance = 0.0
    for (debt in debtList){
        var amount = "${debt.debtSign}${debt.amount}".toDoubleOrNull()
        if (amount != null) {

            balance += amount
        }
    }
    return NumberFormat.getCurrencyInstance().format(balance)
}


data class HomeUiState(val debtList: List<Debt> = listOf(), val balance: String = "0.0")