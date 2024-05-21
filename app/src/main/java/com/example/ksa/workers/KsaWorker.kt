package com.example.ksa.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.ksa.R
import com.example.ksa.data.*
import com.example.ksa.network.KsaApi


class KsaWorker(
    context: Context,
    workerParams: WorkerParameters,

    ) : CoroutineWorker(context, workerParams) {


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val memberId = inputData.getInt("memberId", 0)
        return try {
            val announcements = KsaApi.retrofitService.getAnnouncements(memberId)
            if (announcements.isEmpty()) {
                throw Exception("No announcements")
            }
            val latestAnnouncement = announcements.last()
            notificationBuilder(
                title = latestAnnouncement.translations["nl"]?.title,
                content = latestAnnouncement.translations["nl"]?.message
            )
            Result.success()
        } catch (e: Exception) {
            notificationBuilder(title = "Geen connectie tot de server" , content = "Je zal de laatste update niet ontvangen")
            Result.failure()
        }
    }
    private fun notificationBuilder(title : String?, content : String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Aankondigingen",
                "Aankondigingen",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Aankondigingen van de KSA"
            }
            notificationManager.createNotificationChannel(channel)
            val notification = NotificationCompat.Builder(applicationContext, "Aankondigingen")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ksalogotransparant)
                .build()

            notificationManager.notify(1, notification)
        }
    }
}
