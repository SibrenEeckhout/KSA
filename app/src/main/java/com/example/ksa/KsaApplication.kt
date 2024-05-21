package com.example.ksa

import android.app.Application
import com.example.ksa.data.KsaDatabase
import com.example.ksa.data.TodoDatabase

class KsaApplication: Application() {
    val todoDatabase: TodoDatabase by lazy { TodoDatabase.getDatabase(this) }
    val ksaDatabase: KsaDatabase by lazy { KsaDatabase.getDatabase(this) }
}