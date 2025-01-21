package com.cioffi.debtcalculation.ui.debt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cioffi.debtcalculation.DebtTopAppBar
import com.cioffi.debtcalculation.R
import com.cioffi.debtcalculation.navigation.NavigationDestination
import com.cioffi.debtcalculation.ui.AppViewModelProvider
import com.cioffi.debtcalculation.ui.home.HomeDestination.tilteScreen
import com.cioffi.debtcalculation.ui.theme.DebtCalculationTheme
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

object DebtEntryDestination : NavigationDestination {
    override val route: String = "debt_entry"
    override val tilteScreen: String = "Debt Entry"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtentrySceen(
    navigateBack: () -> Unit,
    onNavigationUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: DebtEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            DebtTopAppBar(
                title = tilteScreen,
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigationUp
            )
        }
    ) { padding ->
        DebtEntryBody(
            debtUiState = viewModel.debtUiState,
            onDebtvalueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveDebt()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    top = padding.calculateTopPadding(),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun DebtEntryBody(
    debtUiState: DebtUiState,
    onDebtvalueChange: (DebtDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(200.dp)
    ) {

        DebtInputForm(
            debtDetails = debtUiState.DebtDetails,
            onValueChange = onDebtvalueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            enabled = debtUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.save_action))
        }
    }
}

@Composable
fun DebtInputForm(
    debtDetails: DebtDetails,
    modifier: Modifier = Modifier,
    onValueChange: (DebtDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = debtDetails.amount,
            onValueChange = { onValueChange(debtDetails.copy(amount = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(R.string.debt_amount)) },
            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = debtDetails.description,
            onValueChange = { onValueChange(debtDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.debt_desc)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        RadioButtonSingleSelection(onValueChange = onValueChange, debtDetails = debtDetails)
        if (false) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun RadioButtonSingleSelection(
    modifier: Modifier = Modifier,
    onValueChange: (DebtDetails) -> Unit,
    debtDetails: DebtDetails,
) {
    val radioOptions = listOf("+", "-")
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptions[0])
    }
    Row(
        modifier
            .selectableGroup()
            .fillMaxWidth(),
        Arrangement.Center
    ) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .wrapContentHeight()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            onValueChange(debtDetails.copy(debtSign = text))
                        },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val clr = if (text == selectedOption)
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .padding(30.dp)
                        .drawBehind {
                            drawCircle(
                                color = clr,
                                radius = 70f
                            )
                        }
                )
//                RadioButton(
//                    selected = (text == selectedOption),
//                    onClick = {
//                        onOptionSelected(text)
//                        onValueChange (debtDetails.copy(debtSign = text))
//                    }
//                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    DebtCalculationTheme {
        DebtEntryBody(debtUiState = DebtUiState(
            DebtDetails(
                dateFormat = "01/05/2025",
                debtSign = "+",
                amount = "60",
                description = "Bonifico"
            )
        ), onDebtvalueChange = {}, onSaveClick = {})
    }
}