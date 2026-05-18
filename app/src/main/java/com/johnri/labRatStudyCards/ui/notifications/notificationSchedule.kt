package com.johnri.labRatStudyCards.ui.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object notificationSchedule {

    fun schedule(context: Context) {

        val prefs = context.getSharedPreferences("labrat_prefs", Context.MODE_PRIVATE)

        val enabled = prefs.getBoolean("notifications_enabled", false)
        val hour = prefs.getInt("notif_hour", 20)
        val minute = prefs.getInt("notif_minute", 0)

        if (!enabled) {
            cancel(context)
            return
        }

        val now = Calendar.getInstance()

        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        val delay = target.timeInMillis - now.timeInMillis

        val request = PeriodicWorkRequestBuilder<studyReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "study_reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("study_reminder")
    }
}