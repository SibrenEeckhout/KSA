@file:OptIn(ExperimentalSerializationApi::class)

package com.example.ksa.network

import com.example.ksa.classes.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


private const val BASE_URL =
    "http://141.134.173.203/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface KsaApiService {
    @GET("members")
    suspend fun getMembers(): List<Member>
    @GET("announcements")
    suspend fun getAnnouncements(@Query("memberId") memberId: Int): List<Announcement>
    @GET("groups")
    suspend fun getGroups(): List<Group>
    @POST("login")
    suspend fun login(@Body loginData: LoginData?): CurrentMember
    @GET("translations")
    suspend fun getTranslations(): List<Translation>
}

object KsaApi {
    val retrofitService : KsaApiService by lazy {
        retrofit.create(KsaApiService::class.java)
    }
}


