package com.cioffi.debtcalculation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cioffi.debtcalculation.ui.debt.DebtEntryDestination
import com.cioffi.debtcalculation.ui.debt.DebtentrySceen
import com.cioffi.debtcalculation.ui.home.HomeDestination
import com.cioffi.debtcalculation.ui.home.HomeScreen

@Composable
fun DebtNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
        ){
        composable(route = HomeDestination.route){
            HomeScreen(
                navigateToDebtEntry = {navController.navigate(DebtEntryDestination.route)}
            )
        }
        composable(route = DebtEntryDestination.route){
            DebtentrySceen(
                navigateBack = { navController.popBackStack() },
                onNavigationUp = { navController.navigateUp() }
            )
        }
    }
}