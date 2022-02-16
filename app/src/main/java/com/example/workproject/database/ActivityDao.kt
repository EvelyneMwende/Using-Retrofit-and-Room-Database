package com.example.workproject.database


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * from activities_table ORDER BY id ASC")
    fun getItems(): Flow<List<ActivityTable>>

    @Query("SELECT * from activities_table WHERE id = :id")
    fun getItem(id: Int): Flow<ActivityTable>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(activity: ActivityTable)

    @Update
    fun update(activity: ActivityTable)

    @Delete
    fun delete(activity: ActivityTable)

}