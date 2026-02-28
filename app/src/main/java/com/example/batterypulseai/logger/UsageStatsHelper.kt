package com.example.batterypulseai.logger

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build

class UsageStatsHelper(private val context: Context) {

    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    /**
     * Determines the current foreground app package name.
     */
    fun getForegroundApp(): String {
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000 * 60 // Look back 1 minute

        val events = usageStatsManager.queryEvents(startTime, endTime)
        var currentApp = "UNKNOWN"
        val event = UsageEvents.Event()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                currentApp = event.packageName
            }
        }
        return currentApp
    }

    /**
     * Helper to get the Standby Bucket for the given package (Android 9+).
     * Buckets relate directly to battery restriction policies.
     */
    fun getAppStandbyBucket(packageName: String): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            usageStatsManager.getAppStandbyBucket(packageName)
        } else {
            -1 // Unsupported
        }
    }
    
    /**
     * Returns a string representation of the bucket.
     */
    fun getBucketName(bucket: Int): String {
        return when (bucket) {
            UsageStatsManager.STANDBY_BUCKET_ACTIVE -> "ACTIVE"
            UsageStatsManager.STANDBY_BUCKET_WORKING_SET -> "WORKING_SET"
            UsageStatsManager.STANDBY_BUCKET_FREQUENT -> "FREQUENT"
            UsageStatsManager.STANDBY_BUCKET_RARE -> "RARE"
            UsageStatsManager.STANDBY_BUCKET_RESTRICTED -> "RESTRICTED"
            else -> "UNKNOWN"
        }
    }
}
