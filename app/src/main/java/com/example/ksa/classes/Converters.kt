package com.example.ksa.classes

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromJson(value: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toJson(value: Map<String, String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromMember(value: String): Member {
        val type = object : TypeToken<Member>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toMember(value: Member): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromGroupEvent(value: String): List<GroupEvent> {
        val type = object : TypeToken<List<GroupEvent>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toGroupEvent(value: List<GroupEvent>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromMemberList(value: String): List<Member> {
        val type = object : TypeToken<List<Member>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromDocumentList(value: String): List<Document> {
        val type = object : TypeToken<List<Document>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toMemberList(value: List<Member>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toDocumentList(value: List<Document>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromAnnouncementList(value: String): List<Announcement> {
        val type = object : TypeToken<List<Announcement>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toAnnouncementList(value: List<Announcement>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromTranslation(value: String):  Map<String,Translation> {
        val type = object : TypeToken< Map<String,Translation>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toTranslation(value:  Map<String,Translation>): String {
        return Gson().toJson(value)
    }

}