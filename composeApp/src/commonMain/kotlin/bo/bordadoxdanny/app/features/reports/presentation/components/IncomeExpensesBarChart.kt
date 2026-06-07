package bo.bordadoxdanny.app.features.reports.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.expenses
import bo.bordadoxdanny.app.income
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.features.reports.presentation.ChartDataPoint
import org.jetbrains.compose.resources.stringResource

@Composable
fun IncomeExpensesBarChart(
    data: List<ChartDataPoint>,
    modifier: Modifier = Modifier,
    barWidth: Dp = 12.dp,
    incomeColor: Color = Color(0xFF4CAF50),
    expenseColor: Color = Color(0xFF2196F3)
) {
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = AppTheme.typography.labelSmall.copy(fontSize = 10.sp, color = AppTheme.colors.textSecondary)

    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(220.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val leftPadding = 45.dp.toPx()
            val bottomPadding = 30.dp.toPx()
            val topPadding = 10.dp.toPx()
            
            val chartWidth = canvasWidth - leftPadding
            val chartHeight = canvasHeight - bottomPadding - topPadding
            
            val maxVal = (data.flatMap { listOf(it.income, it.expenses) }.maxOrNull() ?: 3000.0).coerceAtLeast(1000.0)
            val yLines = listOf(0.0, 500.0, 1000.0, 2000.0, 3000.0).filter { it <= maxVal || it == 3000.0 }
            
            // Draw Y axis lines and labels
            yLines.forEach { value ->
                val y = chartHeight + topPadding - (value.toFloat() / maxVal.toFloat() * chartHeight)
                
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    start = Offset(leftPadding, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1f
                )
                
                val label = if (value >= 1000) "${(value / 1000).toInt()}k" else value.toInt().toString()
                val textLayoutResult = textMeasurer.measure(label, labelStyle)
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(leftPadding - textLayoutResult.size.width - 8.dp.toPx(), y - textLayoutResult.size.height / 2)
                )
            }

            // Draw bars and X labels
            if (data.isNotEmpty()) {
                val itemAreaWidth = chartWidth / data.size
                data.forEachIndexed { index, point ->
                    val xCenter = leftPadding + (index * itemAreaWidth) + (itemAreaWidth / 2)
                    
                    // X Label
                    val textLayoutResult = textMeasurer.measure(point.label, labelStyle)
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(xCenter - textLayoutResult.size.width / 2, chartHeight + topPadding + 8.dp.toPx())
                    )

                    if (point.income > 0 || point.expenses > 0) {
                        val barWidthPx = barWidth.toPx()
                        
                        // Income bar (left)
                        val incomeHeight = (point.income.toFloat() / maxVal.toFloat() * chartHeight)
                        drawRect(
                            color = incomeColor,
                            topLeft = Offset(xCenter - barWidthPx, chartHeight + topPadding - incomeHeight),
                            size = Size(barWidthPx, incomeHeight)
                        )
                        
                        // Expense bar (right)
                        val expenseHeight = (point.expenses.toFloat() / maxVal.toFloat() * chartHeight)
                        drawRect(
                            color = expenseColor,
                            topLeft = Offset(xCenter, chartHeight + topPadding - expenseHeight),
                            size = Size(barWidthPx, expenseHeight)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChartLegendItem(color = incomeColor, label = stringResource(Res.string.income))
            Spacer(modifier = Modifier.width(24.dp))
            ChartLegendItem(color = expenseColor, label = stringResource(Res.string.expenses))
        }
    }
}

@Composable
private fun ChartLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = AppTheme.typography.bodyMedium)
    }
}
