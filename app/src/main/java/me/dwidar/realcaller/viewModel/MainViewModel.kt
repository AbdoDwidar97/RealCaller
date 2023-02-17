package me.dwidar.realcaller.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CallLog
import android.text.format.DateFormat
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.dwidar.realcaller.model.components.MyCallLog
import me.dwidar.realcaller.model.enums.AppCallLogType
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainViewModel : ViewModel()
{
    private val localMyCallLogs : MutableLiveData<HashMap<String, ArrayList<String>>> = MutableLiveData()
    private val localMyCallLogsNumbers : MutableLiveData<ArrayList<MyCallLog>> = MutableLiveData()
    private val clomns = listOf<String> (
        CallLog.Calls._ID,
        CallLog.Calls.NUMBER,
        CallLog.Calls.TYPE,
        CallLog.Calls.DATE,
        CallLog.Calls.CACHED_NAME
    ).toTypedArray()

    fun getCallLogs() : LiveData<HashMap<String, ArrayList<String>>> = localMyCallLogs
    fun getCallLogsNumbers() : LiveData<ArrayList<MyCallLog>> = localMyCallLogsNumbers

    fun checkForCallLogsPermission(context: Context, activity: Activity)
    {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) !=
            PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, Array(1){Manifest.permission.READ_CALL_LOG}, 101)
        }
    }

    @SuppressLint("Recycle")
    fun getCallLogsFromDevice(contentResolver: ContentResolver)
    {
        var result = contentResolver.query(CallLog.Calls.CONTENT_URI,
            clomns, null, null,
            "${CallLog.Calls.DATE} DESC")

        var number = result!!.getColumnIndex(CallLog.Calls.NUMBER)
        var type = result.getColumnIndex(CallLog.Calls.TYPE)
        var callDate = result.getColumnIndex(CallLog.Calls.DATE)
        var contactName = result.getColumnIndex(CallLog.Calls.CACHED_NAME)

        val callLogsHashMap = HashMap<String, ArrayList<String>>()
        val callLogsNumbers = arrayListOf<MyCallLog>()

        while (result.moveToNext())
        {
            val callType = result.getString(type).toInt()
            var stringType = ""
            var dateString = convertTimeStampToDate(result.getString(callDate).toLong())

            stringType = when (callType) {
                CallLog.Calls.OUTGOING_TYPE -> AppCallLogType.OutgoingCall.toString()
                CallLog.Calls.INCOMING_TYPE -> AppCallLogType.ReceivedCall.toString()
                CallLog.Calls.MISSED_TYPE -> AppCallLogType.MissedCall.toString()
                else -> AppCallLogType.OtherCall.toString()
            }

            var nm = result.getString(number)
            if (result.getString(contactName) != null) nm = result.getString(contactName)

            var myCallLog = MyCallLog(nm, result.getString(number), dateString, stringType)
            if (callLogsHashMap.containsKey(myCallLog.contactNumber))
            {
                var callsList = callLogsHashMap[myCallLog.contactNumber]
                callsList!!.add(myCallLog.lastCallDate)
                callLogsHashMap[myCallLog.contactNumber] = callsList
            }
            else
            {
                callLogsHashMap[myCallLog.contactNumber] = arrayListOf(myCallLog.lastCallDate)
                callLogsNumbers.add(myCallLog)
            }
        }

        result.close()
        localMyCallLogs.value = callLogsHashMap
        localMyCallLogsNumbers.value = callLogsNumbers
    }

    private fun convertTimeStampToDate(ts : Long) : String
    {
        //Get instance of calendar
        val calendar = Calendar.getInstance(Locale.getDefault())
        //get current date from ts
        calendar.timeInMillis = ts
        //return formatted date
        return DateFormat.format("dd/MM/yy hh:mm a", calendar).toString()
    }
}