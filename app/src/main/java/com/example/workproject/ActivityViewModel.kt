package com.example.workproject

import com.example.workproject.database.ActivityTable
import com.example.workproject.database.ActivityDao
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope



class ActivityViewModel (private val activityDao: ActivityDao) : ViewModel() {
    val allItems: LiveData<List<ActivityTable>> = activityDao.getItems().asLiveData()


    fun updateItem(
        itemId: Int,
        itemActivity: String,
        itemType: String,
        itemParticipants: Int,
        itemPrice: Double
    ) {
        val updatedItem =
            getUpdatedItemEntry(itemId, itemActivity, itemType, itemParticipants, itemPrice)
        updateItem(updatedItem)
    }

    fun addNewItem(
        itemActivity: String,
        itemType: String,
        itemParticipants: Int,
        itemPrice: Double
    ) {
        val newItem = getNewItemEntry(itemActivity, itemType, itemParticipants, itemPrice)
        insertItem(newItem)
    }

    fun deleteItem(activity: ActivityTable) {
        viewModelScope.launch {
            activityDao.delete(activity)
        }
    }


    fun retrieveItem(id: Int): LiveData<ActivityTable> {
        return activityDao.getItem(id).asLiveData()
    }


    fun isEntryValid(
        itemActivity: String,
        itemType: String,
        itemParticipants: String,
        itemPrice: String
    ): Boolean {
        if (itemActivity.isBlank() || itemType.isBlank() || itemParticipants.isBlank() || itemPrice.isBlank()) {
            return false
        }
        return true
    }


    private fun getNewItemEntry(
        itemActivity: String,
        itemType: String,
        itemParticipants: Int,
        itemPrice: Double
    ): ActivityTable {
        return ActivityTable(
            activityType = itemActivity,
            type = itemType,
            participants = itemParticipants,
            price = itemPrice
        )

    }

    private fun insertItem(activity: ActivityTable) {
        viewModelScope.launch {
            activityDao.insert(activity)
        }
    }


    private fun updateItem(activity: ActivityTable) {
        viewModelScope.launch {
            activityDao.update(activity)
        }
    }
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemActivity: String,
        itemType: String,
        itemParticipants: Int,
        itemPrice: Double
    ): ActivityTable {
        return ActivityTable(
            id = itemId,
            activityType = itemActivity,
            type = itemType,
            participants = itemParticipants,
            price = itemPrice
        )
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