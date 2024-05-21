package com.example.ksa.data

import android.content.Context
import androidx.room.*
import com.example.ksa.classes.Announcement
import com.example.ksa.classes.Converters
import com.example.ksa.classes.CurrentMember
import com.example.ksa.classes.Group
import com.example.ksa.classes.Member
import com.example.ksa.classes.Translation

@Database(entities = [Member::class, Group::class, CurrentMember::class, Announcement::class, Translation::class], version = 11, exportSchema = false)
@TypeConverters(Converters::class)
abstract class KsaMockDatabase : RoomDatabase() {
    abstract fun ksaDao(): KsaDao
    companion object {
        @Volatile
        private var Instance: KsaMockDatabase? = null

        fun getDatabase(context: Context): KsaMockDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, KsaMockDatabase::class.java, "ksa_mock_database")
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}