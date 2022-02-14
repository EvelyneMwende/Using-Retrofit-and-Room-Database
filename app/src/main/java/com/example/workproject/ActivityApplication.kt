package com.example.workproject

import android.app.Application
import com.example.workproject.database.ActivityDatabase

class ActivityApplication: Application() {
    val database: ActivityDatabase by lazy {  ActivityDatabase.getDatabase(this) }
}