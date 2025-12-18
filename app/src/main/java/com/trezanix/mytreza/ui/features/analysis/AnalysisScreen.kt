package com.trezanix.mytreza.ui.features.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.ui.features.analysis.components.*

@Composable
fun AnalysisScreen() {
    val data = remember { AnalysisRepository.getDummyData() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .statusBarsPadding()
    ) {
        PeriodSelector(data.currentMonth)

        LazyColumn(
            contentPadding = PaddingValues(bottom = 100.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                InsightCard(data.insight)
            }

            item {
                CashFlowSummary(data.income, data.expense)
            }

            item {
                MonthlyComparisonCard(
                    currentExpense = data.expense,
                    lastMonthExpense = data.lastMonthExpense
                )
            }

            item {
                ExpenseBreakdownCard(
                    categories = data.categories,
                    totalExpense = data.expense
                )
            }
        }
    }
}