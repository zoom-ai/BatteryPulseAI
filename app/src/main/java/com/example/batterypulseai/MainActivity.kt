package com.example.batterypulseai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.batterypulseai.engine.PredictiveEngine
import com.example.batterypulseai.integration.ModernTechManager
import com.example.batterypulseai.ui.DashboardScreen
import com.example.batterypulseai.ui.theme.BatteryPulseAITheme

class MainActivity : ComponentActivity() {

    private val predictiveEngine = PredictiveEngine()
    private lateinit var modernTechManager: ModernTechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        modernTechManager = ModernTechManager(this)
        
        // Simulating Agent 1 data flowing to Agent 2 and 4
        val simulatedBatteryPct = 76
        val predictedRemainingHrsMins = predictiveEngine.calculateRemainingTime(
            v = simulatedBatteryPct.toDouble(),
            appName = "com.google.android.youtube",
            networkState = "5G",
            isScreenOn = true
        ).toInt() // returns mins
        
        val diagnostics = modernTechManager.analyzeUsagePatterns(
            avgScreenOnHours = 9.2,
            mostUsedBucket = "ACTIVE",
            avgNetworkActivityPercentage = 75.0
        )
        
        // Schedule Agent 4 Battery Saver tasks
        modernTechManager.scheduleBatchedNetworkSync()

        setContent {
            BatteryPulseAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardScreen(
                        currentBatteryPct = simulatedBatteryPct,
                        predictedRemainingMins = predictedRemainingHrsMins,
                        diagnostics = diagnostics
                    )
                }
            }
        }
    }
}
