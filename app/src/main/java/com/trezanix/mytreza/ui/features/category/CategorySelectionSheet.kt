package com.trezanix.mytreza.ui.features.category

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trezanix.mytreza.data.local.entity.CategoryEntity
import com.trezanix.mytreza.ui.common.CategoryIcons
import com.trezanix.mytreza.ui.theme.*

@Composable
fun CategorySelectionSheet(
        onCategorySelected: (CategoryEntity) -> Unit,
        onAddCategoryClick: () -> Unit,
        viewModel: CategoryViewModel = hiltViewModel()
) {
    var selectedType by remember { mutableStateOf("EXPENSE") }
    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val incomeCategories by viewModel.incomeCategories.collectAsState()
    val categoriesToShow = if (selectedType == "EXPENSE") expenseCategories else incomeCategories

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .heightIn(max = 600.dp)
    ) {
        Text(
            text = "Select Category",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = BrandDarkText,
            modifier = Modifier.padding(bottom =16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            TabButton(
                text = "Expense",
                isSelected = selectedType == "EXPENSE",
                onClick = { selectedType = "EXPENSE" },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "Income",
                isSelected = selectedType == "INCOME",
                onClick = { selectedType = "INCOME" },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                AddCategoryItem(onClick = onAddCategoryClick)
            }
            items(categoriesToShow) { category ->
                CategoryItem(
                    category = category,
                    isSelected = false,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable{ onClick() }
            .then(
                if (isSelected) Modifier.shadowCustom(
                    color = Color.Black.copy(alpha = 0.1f),
                    blurRadius = 4.dp
                ) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) BrandDarkText else TextHint
        )
    }
}

fun Modifier.shadowCustom(color: Color, blurRadius: Dp) =this

@Composable
fun CategoryItem(
    category: CategoryEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val categoryColor = try {
        Color(AndroidColor.parseColor(category.colorHex))
    } catch (e: Exception) {
        BrandPrimary
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(categoryColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = CategoryIcons.getIconByName(category.iconName),
                contentDescription = null,
                tint = categoryColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = BrandDarkText,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Icon(Icons.Default.Check, contentDescription = null, tint = BrandPrimary)
        }
    }
}

@Composable
fun AddCategoryItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(1.dp, Color.LightGray, CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = TextHint,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Add New Category",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = TextHint,
            modifier = Modifier.weight(1f)
        )
    }
}