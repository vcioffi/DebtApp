package com.cioffi.debtcalculation.ui.debt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cioffi.debtcalculation.data.Debt
import com.cioffi.debtcalculation.data.DebtsRepository
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DebtEntryViewModel(private val debtsRepository: DebtsRepository) :ViewModel(){
    
        /**
         * Holds current debt ui state
         */
        var debtUiState by mutableStateOf(DebtUiState())
            private set
    
        /**
         * Updates the [debtUiState] with the value provided in the argument. This method also triggers
         * a validation for input values.
         */
        fun updateUiState(DebtDetails: DebtDetails) {
            debtUiState =
                DebtUiState(DebtDetails = DebtDetails, isEntryValid = validateInput(DebtDetails))
        }
    
        suspend fun saveDebt() {
            if (validateInput()) {
                debtsRepository.insertDebt(debtUiState.DebtDetails.toDebt())
            }
        }
    
        private fun validateInput(uiState: DebtDetails = debtUiState.DebtDetails): Boolean {
            return with(uiState) {
              amount.isNotBlank() && debtSign.isNotBlank()
            }
        }
    }
    
    /**
     * Represents Ui State for an debt.
     */


    data class DebtUiState(
        val DebtDetails: DebtDetails = DebtDetails(),
        val isEntryValid: Boolean = false
    )
    
    data class DebtDetails(
        val id: Int = 0,
        val dateFormat :String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
        val description :String = "",
        val debtSign :String = "",
        val amount :String = ""
    )

    fun DebtDetails.toDebt(): Debt = Debt(
        id = id,
        dateFormat = dateFormat,
        description = description,
        debtSign = debtSign,
        amount = amount.toDoubleOrNull() ?: 0.0,
    )
    
    fun Debt.formatedPrice(): String {
        return NumberFormat.getCurrencyInstance().format(amount)
    }

    fun  Debt.dateTransformation() : String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val formattedDate = LocalDateTime.parse(dateFormat, dateFormatter)
    val res = DateTimeFormatter.ofPattern("dd MMMM , yyyy ").format(formattedDate)
    return res
}

    fun Debt.todebtUiState(isEntryValid: Boolean = false): DebtUiState = DebtUiState(
        DebtDetails = this.toDebtDetails(),
        isEntryValid = isEntryValid
    )

    fun Debt.toDebtDetails(): DebtDetails = DebtDetails(
        id = id,
        dateFormat = dateFormat,
        description = description,
        debtSign = debtSign,
        amount = amount.toString()
    )