package com.example.workproject

import com.example.workproject.database.Activity
import com.example.workproject.database.ActivityDao
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope



class ActivityViewModel (private val activityDao: ActivityDao) : ViewModel(){
    val allItems: LiveData<List<Activity>> = activityDao.getItems().asLiveData()

    fun addNewItem(itemActivity: String, itemType: String,itemParticipants: Int, itemPrice: Double) {
        val newItem = getNewItemEntry(itemActivity, itemType,itemParticipants, itemPrice)
        insertItem(newItem)
    }

    private fun getNewItemEntry(itemActivity: String, itemType: String, itemParticipants: Int, itemPrice: Double): Activity {
        return Activity(
            activity = itemActivity,
            type = itemType,
            participants = itemParticipants,
            price = itemPrice
        )

    }

    private fun insertItem(activity: Activity) {
        viewModelScope.launch {
           activityDao.insert(activity)
        }
    }

}

class ActivityViewModelFactory(private val activityDao: ActivityDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivityViewModel(activityDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}