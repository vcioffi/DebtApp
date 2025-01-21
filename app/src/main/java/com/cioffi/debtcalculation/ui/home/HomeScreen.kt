package com.cioffi.debtcalculation.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cioffi.debtcalculation.DebtTopAppBar
import com.cioffi.debtcalculation.R
import com.cioffi.debtcalculation.data.Debt
import com.cioffi.debtcalculation.navigation.NavigationDestination
import com.cioffi.debtcalculation.ui.AppViewModelProvider
import com.cioffi.debtcalculation.ui.debt.dateTransformation
import com.cioffi.debtcalculation.ui.debt.formatedPrice
import com.cioffi.debtcalculation.ui.home.HomeDestination.tilteScreen
import com.cioffi.debtcalculation.ui.theme.DebtCalculationTheme

object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val tilteScreen: String = "Debts"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToDebtEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DebtTopAppBar(
                title = tilteScreen,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToDebtEntry) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Debt/Credit")
            }
        }
    )
    { padding ->
        DebtApp(
            homeUiState = homeUiState,
            contentPadding = padding,
            onDelete = viewModel::deleteUiDebtState
        )
    }
}


@Composable
fun DebtApp(
    homeUiState: HomeUiState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onDelete: (Debt) -> Unit

) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxSize()
    ) {
        if (homeUiState.debtList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.no_debt_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(contentPadding),
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                DebtList(
                    debtList = homeUiState.debtList,
                    onDebySwipe = onDelete,
                    contentPadding = contentPadding,
                    modifier = Modifier
                        .wrapContentWidth()
                )

            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                DebtRecap(
                    homeUiState.balance, modifier = Modifier
                        .wrapContentWidth()
                        .padding(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtList(
    debtList: List<Debt>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onDebySwipe: (Debt) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = debtList, key = { it.id }) { debt ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        onDebySwipe(debt)
                        true
                    } else {
                        false
                    }
                })
            SwipeToDismissBox(
                enableDismissFromEndToStart = true,
                enableDismissFromStartToEnd = false,
                modifier = modifier
                    .animateContentSize()
                    .padding(top = 10.dp),
                state = dismissState,
                backgroundContent = {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .background(MaterialTheme.colorScheme.error)
                                .clip(shape = RoundedCornerShape(20.dp)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(end = 10.dp),
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Favorite Icon",
                                tint = MaterialTheme.colorScheme.onError
                            )
                        }
                    }

                }) {
                DebtCard(
                    debt = debt,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun DebtCard(debt: Debt, modifier: Modifier = Modifier) {
    val signColor = if (debt.debtSign == "+") Color.Green else Color.Red
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(modifier.align(alignment = Alignment.CenterHorizontally)) {
            Text(
                text = debt.dateTransformation(),
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
            )
        }
        Row(Modifier.align(alignment = Alignment.CenterHorizontally)) {
            Text(
                text = "${debt.debtSign} ${debt.formatedPrice()}",
                style = MaterialTheme.typography.bodyMedium,
                color = signColor,
                fontSize = 25.sp
            )
        }
        Row {
            Text(
                text = debt.description,
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun DebtRecap(balance: String, modifier: Modifier) {
    val balanceColor = if (balance.contains("-")) {
        Color.Red
    } else {
        Color.Green
    }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 40.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Balance",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding(top = 20.dp, bottom = 5.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Row(modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = balance,
                color = balanceColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding(top = 10.dp, bottom = 20.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    DebtCalculationTheme {
        DebtApp(
            homeUiState = HomeUiState(
                debtList = listOf(
                    Debt(0, "2025-01-20T15:34:50Z", "Bonifico", "-", 50.1),
                    Debt(1, "2024-01-20T15:34:50Z", "Bonifico", "+", 50.1),
                    Debt(2, "2023-01-20T15:34:50Z", "Bonifico", "-", 50.1)
                ),
                balance = "50,00$"
            ), onDelete = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AppPreviewEmptyList() {
    DebtCalculationTheme {
        DebtApp(
            homeUiState = HomeUiState(
                debtList = listOf(),
                balance = "50,00$"
            ), onDelete = {}
        )
    }
}