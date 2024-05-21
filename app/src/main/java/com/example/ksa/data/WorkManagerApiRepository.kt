package com.example.ksa.data

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ksa.workers.KsaWorker
import java.util.concurrent.TimeUnit

class WorkManagerApiRepository(private val context: Context) {


    fun sendRecentAnnouncementNotification(memberId: Int) {

        val inputData = Data.Builder()
            .putInt("memberId", memberId)
            .build()

        val worker = OneTimeWorkRequestBuilder<KsaWorker>()
            .setInputData(inputData)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(worker)
    }
}