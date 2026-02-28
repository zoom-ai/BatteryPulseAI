package com.example.batterypulseai.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.batterypulseai.ui.theme.Typography

data class UsageData(val label: String, val percentage: Float, val color: Color)

@Composable
fun StateDistributionChart(title: String, data: List<UsageData>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stacked Bar representation
            Canvas(modifier = Modifier.fillMaxWidth().height(24.dp).clip(RoundedCornerShape(12.dp))) {
                var currentX = 0f
                val totalWidth = size.width
                
                data.forEach { item ->
                    val segmentWidth = totalWidth * (item.percentage / 100f)
                    drawRect(
                        color = item.color,
                        topLeft = Offset(currentX, 0f),
                        size = Size(segmentWidth, size.height)
                    )
                    currentX += segmentWidth
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Legend
            data.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(12.dp).background(item.color, RoundedCornerShape(2.dp)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(item.label, modifier = Modifier.weight(1f), fontSize = 14.sp)
                    Text("${item.percentage.toInt()}%", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun ZipfsLawChart() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("App Usage Clustering (Zipf's Law)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Simulated Zipf's Law distribution line chart
            val points = listOf(1f, 0.5f, 0.33f, 0.25f, 0.2f, 0.16f, 0.14f)
            Canvas(modifier = Modifier.fillMaxWidth().height(150.dp).padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 24.dp)) {
                val width = size.width
                val height = size.height
                val pointSpacing = width / (points.size - 1)
                
                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = Color(0xFF2962FF),
                        start = Offset(i * pointSpacing, height - (points[i] * height)),
                        end = Offset((i + 1) * pointSpacing, height - (points[i+1] * height)),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawCircle(
                        color = Color(0xFFD50000),
                        radius = 4.dp.toPx(),
                        center = Offset(i * pointSpacing, height - (points[i] * height))
                    )
                }
                // Last point circle
                drawCircle(
                    color = Color(0xFFD50000),
                    radius = 4.dp.toPx(),
                    center = Offset((points.size - 1) * pointSpacing, height - (points.last() * height))
                )
            }
            Text("Rank vs Frequency (Top Apps dominate usage pattern)", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
