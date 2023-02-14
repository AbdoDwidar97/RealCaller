package me.dwidar.realcaller.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.dwidar.realcaller.model.components.MyCallLog

class ContactDetailsViewModel : ViewModel()
{
    private val selectedContact = MutableLiveData<MyCallLog>()
    val CALL_LOGS_LIST_LENGTH = 6

    fun getSelectedContact() : LiveData<MyCallLog> = selectedContact

    fun selectContact(myCallLog: MyCallLog)
    {
        selectedContact.value = myCallLog
        Log.d("TAG", "selectContact: ${selectedContact.value!!.contactName}")
    }
}