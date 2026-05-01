package bo.bordadoxdanny.app.core.daemon.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bo.bordadoxdanny.app.core.daemon.watchdog.WatchdogState
import bo.bordadoxdanny.app.core.daemon.watchdog.WatchdogViewModel
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@Composable
fun DaemonStatusScreen(viewModel: WatchdogViewModel) {
    val state by viewModel.watchdogState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Status indicator circle
        val indicatorColor = when (state) {
            is WatchdogState.Healthy -> AppTheme.colors.primary
            is WatchdogState.Failure -> Color(0xFFB00020)
            is WatchdogState.Recovering -> Color(0xFFFFBF00) // Amber
            else -> AppTheme.colors.textPrimary.copy(alpha = 0.3f)
        }

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(indicatorColor)
        )

        Spacer(modifier = Modifier.height(24.dp))

        val statusLabel = when (state) {
            is WatchdogState.Healthy -> "HEALTHY"
            is WatchdogState.Failure -> "FAILURE"
            is WatchdogState.Recovering -> "RECOVERING"
            else -> "IDLE"
        }

        Text(
            text = statusLabel,
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state is WatchdogState.Healthy) {
            val lastSeen = (state as WatchdogState.Healthy).lastSeen
            val now = Clock.System.now().toEpochMilliseconds()
            val minsAgo = (now - lastSeen) / (1000 * 60)
            Text(
                text = "Last heartbeat: $minsAgo mins ago",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textPrimary.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))

        if (state is WatchdogState.Failure) {
            PrimaryButton(
                text = "Force Repair",
                onClick = {
                    scope.launch {
                        viewModel.autoRepair()
                    }
                }
            )
        }
    }
}
