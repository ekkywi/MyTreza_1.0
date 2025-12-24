package com.trezanix.mytreza.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object CategoryIcons {
    fun getIconByName(name: String): ImageVector {
        return when (name) {
            "fastfood" -> Icons.Default.Fastfood
            "commute" -> Icons.Default.DirectionsBus
            "shopping_bag" -> Icons.Default.ShoppingBag
            "movie" -> Icons.Default.Movie
            "medical_services" -> Icons.Default.MedicalServices
            "school" -> Icons.Default.School
            "home" -> Icons.Default.Home
            "pets" -> Icons.Default.Pets
            "payments" -> Icons.Default.Payments
            "card_giftcard" -> Icons.Default.CardGiftcard

            else -> Icons.Default.Category
        }
    }

    val availableIcons = mapOf(
        "fastfood" to Icons.Default.Fastfood,
        "restaurant" to Icons.Default.Restaurant,
        "lunch_dining" to Icons.Default.LunchDining,
        "commute" to Icons.Default.DirectionsBus,
        "directions_car" to Icons.Default.DirectionsCar,
        "flight" to Icons.Default.Flight,
        "shopping_bag" to Icons.Default.ShoppingBag,
        "shopping_cart" to Icons.Default.ShoppingCart,
        "movie" to Icons.Default.Movie,
        "sports_esports" to Icons.Default.SportsEsports,
        "fitness_center" to Icons.Default.FitnessCenter,
        "medical_services" to Icons.Default.MedicalServices,
        "school" to Icons.Default.School,
        "home" to Icons.Default.Home,
        "work" to Icons.Default.Work,
        "payments" to Icons.Default.Payments,
        "star" to Icons.Default.Star
    )
}