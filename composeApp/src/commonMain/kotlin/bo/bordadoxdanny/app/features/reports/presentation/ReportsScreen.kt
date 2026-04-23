package bo.bordadoxdanny.app.features.reports.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.a11y_report_item
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// TalkBack announces: "Reportes, heading"
// Each item: "Reporte: [Title]. [Content]"
@Composable
fun ReportsScreen(viewModel: ReportsViewModel = koinViewModel()) {
    val reports by viewModel.reports.collectAsState()

    Column {
        Text(
            text = "Reportes", 
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(reports) { report ->
                HorizontalDivider()
                
                val contentDesc = stringResource(
                    Res.string.a11y_report_item,
                    report.title,
                    report.content
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .semantics(mergeDescendants = true) {
                            contentDescription = contentDesc
                        }
                ) {
                    Text(
                        text = report.title, 
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.textPrimary
                    )
                    Text(
                        text = report.content, 
                        style = AppTheme.typography.labelLarge,
                        color = AppTheme.colors.textPrimary.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
