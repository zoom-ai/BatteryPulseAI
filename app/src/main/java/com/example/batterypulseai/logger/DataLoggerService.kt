package com.example.batterypulseai.logger

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DataLoggerService : Service() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var usageStatsHelper: UsageStatsHelper
    
    // States
    private var batteryLevel: Int = -1
    private var batteryStatus: Int = -1
    private var isScreenOn: Boolean = true
    
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                }
                Intent.ACTION_SCREEN_ON -> isScreenOn = true
                Intent.ACTION_SCREEN_OFF -> isScreenOn = false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        usageStatsHelper = UsageStatsHelper(this)
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(receiver, filter)
        startForeground(1, createNotification())
        startLogging()
    }

    private fun startLogging() {
        scope.launch {
            while (isActive) {
                logCurrentState()
                delay(1000) // 1 second intervals
            }
        }
    }
    
    private fun logCurrentState() {
        val networkState = getNetworkState()
        val isGpsOn = getGpsState()
        val isBluetoothOn = getBluetoothState()
        
        val fgApp = usageStatsHelper.getForegroundApp()
        val bucket = usageStatsHelper.getBucketName(usageStatsHelper.getAppStandbyBucket(fgApp))
        
        // Log locally or pass to Agent 2 (Predictive Engine)
        // System.out.println("LOG: Battery: $batteryLevel%, Screen: $isScreenOn, Network: $networkState, GPS: $isGpsOn, BT: $isBluetoothOn, App: $fgApp ($bucket)")
    }
    
    private fun getNetworkState(): String {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork ?: return "NONE"
        val caps = cm.getNetworkCapabilities(activeNetwork) ?: return "NONE"
        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
            else -> "OTHER"
        }
    }
    
    private fun getGpsState(): Boolean {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    
    private fun getBluetoothState(): Boolean {
        val bm = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bm.adapter?.isEnabled == true
    }

    private fun createNotification(): Notification {
        val channelId = "DataLoggerChannel"
        val channel = NotificationChannel(
            channelId, "Data Logger", NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
        
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("BatteryPulseAI")
            .setContentText("Monitoring battery usage patterns...")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        job.cancel()
    }
}
