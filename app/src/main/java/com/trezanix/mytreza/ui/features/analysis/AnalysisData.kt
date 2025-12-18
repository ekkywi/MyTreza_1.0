package com.trezanix.mytreza.ui.features.analysis

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


enum class InsightType { GOOD, WARNING, DANGER }

data class InsightData(
    val title: String,
    val message: String,
    val type: InsightType
)

data class CategoryExpense(
    val name: String,
    val amount: Double,
    val color: Color,
    val icon: ImageVector,
    val percent: Float
)

data class AnalysisState(
    val currentMonth: String,
    val income: Double,
    val expense: Double,
    val lastMonthExpense: Double,
    val insight: InsightData,
    val categories: List<CategoryExpense>
)

object AnalysisRepository {
    fun getDummyData(): AnalysisState {
        val income = 15000000.0
        val expense = 8500000.0
        val lastMonthExpense = 7200000.0

        val categories = listOf(
            CategoryExpense("Makanan", 3500000.0, Color(0xFFFFA726), Icons.Default.Fastfood, 0.41f),
            CategoryExpense("Transport", 1500000.0, Color(0xFF42A5F5), Icons.Default.DirectionsCar, 0.17f),
            CategoryExpense("Belanja", 2000000.0, Color(0xFFAB47BC), Icons.Default.ShoppingBag, 0.23f),
            CategoryExpense("Rumah", 1500000.0, Color(0xFF66BB6A), Icons.Default.Home, 0.17f)
        )

        val insight = when {
            expense > income -> InsightData("Awas Defisit!", "Pengeluaranmu lebih besar dari pemasukan.", InsightType.DANGER)
            expense > (income * 0.8) -> InsightData("Hati-hati!", "Kamu sudah menghabiskan 80% pemasukan.", InsightType.WARNING)
            else -> InsightData("Keuangan Sehat", "Kamu berhasil hemat bulan ini. Mantap!", InsightType.GOOD)
        }

        return AnalysisState(
            currentMonth = "Desember 2025",
            income = income,
            expense = expense,
            lastMonthExpense = lastMonthExpense,
            insight = insight,
            categories = categories
        )
    }
}