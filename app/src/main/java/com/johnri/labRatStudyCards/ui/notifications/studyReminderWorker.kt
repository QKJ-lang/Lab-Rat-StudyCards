package com.johnri.labRatStudyCards.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.database.appDatabase
import com.johnri.labRatStudyCards.ui.home.homeActivity

class studyReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val db = appDatabase.getDatabase(applicationContext)
        val cardDao = db.studyCardDao()

        val now = System.currentTimeMillis()


        val pendingCards = cardDao.getPendingToday(now)

        if (pendingCards > 0) {
            showNotification(pendingCards)
        }

        return Result.success()
    }

    private fun showNotification(pendingCards: Int) {

        val channelId = "study_reminder"

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios de estudio",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, homeActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("LabRat: ¡Tienes tarjetas pendientes!")
            .setContentText("Tienes $pendingCards tarjetas para repasar")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}