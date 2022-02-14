package com.example.workproject.database


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * from activities_table ORDER BY activity ASC")
    fun getItems(): Flow<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(activity: Activity)

}