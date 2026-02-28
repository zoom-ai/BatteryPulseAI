package com.example.batterypulseai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.batterypulseai.integration.ModernTechManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    currentBatteryPct: Int,
    predictedRemainingMins: Int,
    diagnostics: ModernTechManager.BatteryDiagnostics
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BatteryPulseAI", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { RemainingTimeCard(currentBatteryPct, predictedRemainingMins) }
            item { DiagnosticsCard(diagnostics) }
            // Render actual charts
            item { 
                StateDistributionChart(
                    title = "Network Distribution (Agent 1 Log)",
                    data = listOf(
                        UsageData("5G/Cellular", 65f, Color(0xFF2962FF)), // Primary
                        UsageData("Wi-Fi", 25f, Color(0xFF00C853)), // Secondary
                        UsageData("Idle/None", 10f, Color.Gray)
                    )
                ) 
            }
            item { ZipfsLawChart() }
            
            item {
                Button(
                    onClick = { /* Check Logs */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Raw Agent Logs")
                }
            }
        }
    }
}

@Composable
fun RemainingTimeCard(batteryPct: Int, remainingMins: Int) {
    val hrs = remainingMins / 60
    val mins = remainingMins % 60
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$batteryPct%",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Predicted Remaining Time",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Text(
                text = "${hrs}h ${mins}m",
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DiagnosticsCard(diagnostics: ModernTechManager.BatteryDiagnostics) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Agent 4 Diagnostics", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            
            Text("Profile: ${diagnostics.tendencyProfile}", fontWeight = FontWeight.SemiBold)
            Text("Top Drain: ${diagnostics.topDrainSource}", color = MaterialTheme.colorScheme.error)
            
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
            
            Text("AI Recommendations:", fontWeight = FontWeight.SemiBold)
            diagnostics.recommendations.forEach { rec ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("•", modifier = Modifier.padding(end = 8.dp))
                    Text(rec, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun ChartPlaceholder(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}
