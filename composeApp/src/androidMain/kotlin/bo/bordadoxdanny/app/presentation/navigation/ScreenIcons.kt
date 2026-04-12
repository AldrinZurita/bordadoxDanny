package bo.bordadoxdanny.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

fun Screen.getIcon(): ImageVector {
    return when (this) {
        Screen.Orders -> Icons.Filled.ShoppingCart
        Screen.Cash -> Icons.Filled.List
        Screen.Reports -> Icons.Filled.Notifications
        Screen.Profile -> Icons.Filled.AccountCircle
    }
}
