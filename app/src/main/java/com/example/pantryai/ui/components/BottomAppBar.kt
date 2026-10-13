
package com.example.pantryai.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.pantryai.navigation.Screen
import com.example.pantryai.ui.theme.PantryGreen
import com.example.pantryai.ui.theme.PantryGreenLight

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.HOME,
        label = "Trang chủ",
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        route = Screen.RECIPES,
        label = "Công thức",
        icon = Icons.Default.Book
    ),
    BottomNavItem(
        route = Screen.ASSISTANT,
        label = "Trợ lý AI",
        icon = Icons.Default.AutoAwesome
    ),
    BottomNavItem(
        route = Screen.FAVORITES,
        label = "Yêu thích",
        icon = Icons.Default.Favorite
    )
)

@Composable
fun PantryBottomAppBar(
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = PantryGreen
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentDestination
                ?.hierarchy
                ?.any { destination ->
                    destination.route == item.route
                } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PantryGreen,
                    selectedTextColor = PantryGreen,
                    indicatorColor = PantryGreenLight,
                    unselectedIconColor = Color(0xFF8A948D),
                    unselectedTextColor = Color(0xFF8A948D)
                )
            )
        }
    }
}
