package com.trezanix.mytreza.ui.features.dashboard.components

import androidx.compose.ui.graphics.Color
import com.trezanix.mytreza.ui.theme.BrandPrimary

enum class AssetType {
    NET_WORTH, FIAT, STOCK, GOLD, CRYPTO
}

data class AssetAllocation(
    val label: String,
    val percentage: Float,
    val color: Color
)

data class BankAccount(
    val name: String,
    val balance: String,
    val color: Color
)

data class StockItem(
    val ticker: String,
    val name: String,
    val value: String,
    val changePercent: String,
    val isUp: Boolean
)

data class GoldItem(
    val label: String,
    val weight: String,
    val value: String
)

data class CryptoItem(
    val symbol: String,
    val name: String,
    val amountHeld: String,
    val valueIdr: String,
    val change24h: String,
    val isUp: Boolean
)

data class AssetPortfolio(
    val type: AssetType,
    val title: String,
    val totalValue: String,
    val currencyCode: String,
    val growthPercent: String,
    val isPositive: Boolean,

    val allocations: List<AssetAllocation> = emptyList(),
    val bankAccounts: List<BankAccount> = emptyList(),
    val stocks: List<StockItem> = emptyList(),
    val goldItems: List<GoldItem> = emptyList(),
    val cryptoItems: List<CryptoItem> = emptyList()
)

fun AssetType.getGradientColors(): List<Color> {
    return when (this) {
        AssetType.NET_WORTH -> listOf(
            BrandPrimary.copy(alpha = 0.9f),
            Color(0xFF4A00E0).copy(alpha = 0.8f)
        )
        AssetType.FIAT -> listOf(
            Color(0xFF11998e).copy(alpha = 0.9f),
            Color(0xFF38ef7d).copy(alpha = 0.8f)
        )

        AssetType.STOCK -> listOf(
            Color(0xFF0F2027).copy(alpha = 0.95f),
            Color(0xFF203A43).copy(alpha = 0.9f),
            Color(0xFF2C5364).copy(alpha = 0.85f)
        )

        AssetType.GOLD -> listOf(
            Color(0xFFD38312).copy(alpha = 0.95f),
            Color(0xFFE6904E),
            Color(0xFFF2C94C).copy(alpha = 0.8f)
        )

        AssetType.CRYPTO -> listOf(
            Color(0xFF8E2DE2).copy(alpha = 0.9f),
            Color(0xFF4A00E0).copy(alpha = 0.8f)
        )
    }
}

fun getMockPortfolios(): List<AssetPortfolio> {
    return listOf(
        // 1. NET WORTH
        AssetPortfolio(
            type = AssetType.NET_WORTH,
            title = "Total Net Worth",
            totalValue = "12.450.000.000",
            currencyCode = "IDR",
            growthPercent = "2.5%",
            isPositive = true,
            allocations = listOf(
                AssetAllocation("Cash", 0.1f, Color(0xFF00E676)),
                AssetAllocation("Saham", 0.4f, Color(0xFFE53935)),
                AssetAllocation("Emas", 0.2f, Color(0xFFFFD700)),
                AssetAllocation("Crypto", 0.3f, Color(0xFFBB86FC))
            )
        ),

        // 2. CASH
        AssetPortfolio(
            type = AssetType.FIAT,
            title = "Cash & Bank",
            totalValue = "450.000.000",
            currencyCode = "IDR",
            growthPercent = "0.5%",
            isPositive = true,
            bankAccounts = listOf(
                BankAccount("BCA", "350.000.000", Color(0xFF005CAA)),
                BankAccount("Mandiri", "95.000.000", Color(0xFFFFC107)),
                BankAccount("GoPay", "5.000.000", Color(0xFF00AED5))
            )
        ),

        // 3. SAHAM
        AssetPortfolio(
            type = AssetType.STOCK,
            title = "Portofolio Saham",
            totalValue = "5.200.000.000",
            currencyCode = "IDR",
            growthPercent = "1.8%",
            isPositive = true,
            stocks = listOf(
                StockItem("BBCA", "Bank Central Asia", "2.500.000.000", "+1.2%", true),
                StockItem("TLKM", "Telkom Indonesia", "1.200.000.000", "-0.5%", false),
                StockItem("ADRO", "Adaro Energy", "1.500.000.000", "+5.4%", true)
            )
        ),

        // 4. EMAS
        AssetPortfolio(
            type = AssetType.GOLD,
            title = "Emas & Logam Mulia",
            totalValue = "2.500.000.000",
            currencyCode = "IDR",
            growthPercent = "0.2%",
            isPositive = true,
            goldItems = listOf(
                GoldItem("Antam Certieye", "500g", "650.000.000"),
                GoldItem("UBS Gold", "100g", "128.000.000"),
                GoldItem("Tabungan Emas", "1.5kg", "1.722.000.000")
            )
        ),

        AssetPortfolio(
            type = AssetType.CRYPTO,
            title = "Aset Kripto",
            totalValue = "1.25",
            currencyCode = "BTC",
            growthPercent = "5.4%",
            isPositive = false,
            cryptoItems = listOf(
                CryptoItem("BTC", "Bitcoin", "1.05 BTC", "1.500.000.000", "-2.1%", false),
                CryptoItem("ETH", "Ethereum", "15.4 ETH", "600.000.000", "+1.4%", true),
                CryptoItem("SOL", "Solana", "240 SOL", "450.000.000", "+12.5%", true)
            )
        )
    )
}