package com.trezanix.mytreza.ui.features.dashboard.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trezanix.mytreza.R
import com.trezanix.mytreza.ui.theme.BrandPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardMenuSection(
    userMenuList: List<TrezaMenuItem> = MenuProvider.allMenus,
    onMenuClick: (FeatureID) -> Unit,
    onEditMenuClick: () -> Unit
) {
    val context = LocalContext.current

    var showMoreSheet by remember { mutableStateOf(false) }

    val quickMenus = remember(userMenuList) {
        if (userMenuList.size <= 8) {
            userMenuList
        } else {
            userMenuList.take(7) + MenuProvider.moreItem
        }
    }

    fun handleItemClick(item: TrezaMenuItem) {
        if (item.id == FeatureID.MORE) {
            showMoreSheet = true
        }
        else if (item.isComingSoon) {
            val featureName = context.getString(item.title)
            val message = context.getString(R.string.dashboard_toast_coming_soon, featureName)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        else {
            onMenuClick(item.id)
        }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(quickMenus) { item ->
                MenuIconItem(item = item, onClick = {
                    handleItemClick(item)
                })
            }
        }
    }

    if (showMoreSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMoreSheet = false },
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 48.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_menu_all_features),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                    OutlinedButton(
                        onClick = {
                            showMoreSheet = false
                            onEditMenuClick()
                        },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp),
                        border = BorderStroke(1.dp, BrandPrimary)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = BrandPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.dashboard_menu_set), style = MaterialTheme.typography.labelMedium, color = BrandPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(userMenuList) { item ->
                        MenuIconItem(item = item, onClick = {
                            showMoreSheet = false
                            if (item.isComingSoon) {
                                val featureName = context.getString(item.title)
                                val message = context.getString(R.string.dashboard_toast_coming_soon, featureName)
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            } else {
                                onMenuClick(item.id)
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun MenuIconItem(
    item: TrezaMenuItem,
    onClick: () -> Unit
) {
    val labelColor = Color(0xFF333333)

    val moreIconColor = Color(0xFF424242)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (item.id == FeatureID.MORE) Color.Black.copy(alpha = 0.05f)
                    else item.color.copy(alpha = 0.1f)
                )
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(item.title),
                tint = if (item.id == FeatureID.MORE) moreIconColor else item.color,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(item.title),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = labelColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}