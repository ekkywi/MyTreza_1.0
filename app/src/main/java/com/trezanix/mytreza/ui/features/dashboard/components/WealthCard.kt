package com.trezanix.mytreza.ui.features.dashboard.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trezanix.mytreza.ui.theme.BrandPrimary
import com.trezanix.mytreza.utils.formatCurrencyDisplay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WealthCard() {
    val portfolios = remember { getMockPortfolios() }
    var isBalanceVisible by remember { mutableStateOf(true) }
    val pagerState = rememberPagerState(pageCount = { portfolios.size })

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            PortfolioCardItem(
                portfolio = portfolios[page],
                isVisible = isBalanceVisible,
                onToggleVisibility = { isBalanceVisible = !isBalanceVisible }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            repeat(portfolios.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) BrandPrimary else Color.Gray.copy(alpha = 0.3f)
                val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .height(6.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@Composable
fun PortfolioCardItem(
    portfolio: AssetPortfolio,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.4f), Color.White.copy(alpha = 0.1f))
                ),
                shape = RoundedCornerShape(26.dp)
            ),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(colors = portfolio.type.getGradientColors()))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = portfolio.currencyCode,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = portfolio.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    IconButton(onClick = onToggleVisibility, modifier = Modifier.size(20.dp)) {
                        Icon(
                            imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Column(modifier = Modifier.offset(y = (-8).dp)) {
                    Text(
                        text = if (isVisible) {
                            formatCurrencyDisplay(portfolio.totalValue, portfolio.currencyCode)
                        } else "•••••••••••",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (portfolio.totalValue.length > 12) 24.sp else 28.sp
                        ),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = if (portfolio.isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = if (portfolio.isPositive) Color(0xFF00E676) else Color(0xFFFF5252),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${if (portfolio.isPositive) "+" else ""}${portfolio.growthPercent}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }

                Box(contentAlignment = Alignment.BottomStart) {
                    when (portfolio.type) {
                        AssetType.NET_WORTH -> CompositionBarFooter(portfolio.allocations)
                        AssetType.FIAT -> BankBreakdownFooter(portfolio.bankAccounts, portfolio.currencyCode)
                        AssetType.STOCK -> StockBreakdownFooter(portfolio.stocks)
                        AssetType.GOLD -> GoldBreakdownFooter(portfolio.goldItems)
                        AssetType.CRYPTO -> CryptoBreakdownFooter(portfolio.cryptoItems)
                    }
                }
            }
        }
    }
}

@Composable
fun CompositionBarFooter(allocations: List<AssetAllocation>) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.2f))
        ) {
            allocations.forEach { allocation ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(allocation.percentage)
                        .background(allocation.color)
                )
                Spacer(modifier = Modifier.width(2.dp).background(Color.Transparent))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            allocations.forEach { allocation ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(allocation.color, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${allocation.label} ${(allocation.percentage * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun BankBreakdownFooter(accounts: List<BankAccount>, currencyCode: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        accounts.take(2).forEach { account ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(account.color, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        ),
                        color = Color.White.copy(alpha = 0.95f)
                    )
                }
                Text(
                    text = formatCurrencyDisplay(account.balance, currencyCode),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = Color.White
                )
            }
        }
        if (accounts.size > 2) {
            Text(
                text = "+ ${accounts.size - 2} bank lainnya...",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun StockBreakdownFooter(stocks: List<StockItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        stocks.take(2).forEach { stock ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stock.ticker,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        ),
                        color = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatCurrencyDisplay(stock.value, "IDR"),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = if (stock.isUp) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = null,
                        tint = if (stock.isUp) Color(0xFF00E676) else Color(0xFFFF5252),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
        if (stocks.size > 2) {
            Text(
                text = "+ ${stocks.size - 2} emiten lainnya...",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun GoldBreakdownFooter(items: List<GoldItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.take(2).forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(width = 4.dp, height = 16.dp).clip(RoundedCornerShape(2.dp)).background(Color(0xFFFFD700)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${item.label} • ${item.weight}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        ),
                        color = Color.White.copy(alpha = 0.95f)
                    )
                }
                Text(
                    text = formatCurrencyDisplay(item.value, "IDR"),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = Color.White
                )
            }
        }
        if (items.size > 2) {
            Text(
                text = "+ ${items.size - 2} items...",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun CryptoBreakdownFooter(items: List<CryptoItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.take(2).forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Text(
                            text = item.symbol.take(1),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = item.symbol,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = item.amountHeld,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatCurrencyDisplay(item.valueIdr, "IDR"),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = Color.White
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (item.isUp) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = if (item.isUp) Color(0xFF00E676) else Color(0xFFFF5252),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = item.change24h,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = if (item.isUp) Color(0xFF00E676) else Color(0xFFFF5252)
                        )
                    }
                }
            }
        }
        if (items.size > 2) {
            Text(
                text = "+ ${items.size - 2} aset crypto lainnya...",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 42.dp)
            )
        }
    }
}