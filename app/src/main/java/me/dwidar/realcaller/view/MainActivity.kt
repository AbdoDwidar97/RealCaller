package me.dwidar.realcaller.view
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import me.dwidar.realcaller.databinding.ActivityMainBinding
import me.dwidar.realcaller.databinding.MainActionBarBinding
import me.dwidar.realcaller.model.adapters.CallLogsAdapter
import me.dwidar.realcaller.model.components.AppConstants
import me.dwidar.realcaller.model.components.MyCallLog
import me.dwidar.realcaller.model.interfaces.CallLogActionListener
import me.dwidar.realcaller.model.interfaces.OnMakePhoneCall
import me.dwidar.realcaller.viewModel.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var customActionBarBinding: MainActionBarBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var callLogsAdapter : CallLogsAdapter
    private lateinit var selectedPhoneNumber : String

    @Inject
    lateinit var appConstants : AppConstants
    @Inject
    lateinit var onMakePhoneCall: OnMakePhoneCall

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        customActionBarBinding = MainActionBarBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setSupportActionBar(customActionBarBinding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.checkForCallLogsPermission(applicationContext, this)
        mainViewModel.getCallLogsFromDevice(contentResolver)

        mainViewModel.getCallLogsNumbers().observe(this)
        {
            callLogsAdapter = CallLogsAdapter(it, object : CallLogActionListener {
                override fun onCallLogItemClick(phoneNumber: String)
                {
                    callLogItemClick(phoneNumber)
                }

                override fun onCallLogContactDetailsClick(itemIdx : Int)
                {
                    callLogContactDetailsClick(it, itemIdx)
                }
            })

            mainBinding.listCallLogs.adapter = callLogsAdapter
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            mainViewModel.getCallLogsFromDevice(contentResolver)
        }

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onMakePhoneCall.makePhoneCall(this, selectedPhoneNumber)
        }
    }

    private fun callLogItemClick(phoneNumber: String)
    {
        selectedPhoneNumber = phoneNumber
        onMakePhoneCall.makePhoneCall(this@MainActivity, phoneNumber)
    }

    private fun callLogContactDetailsClick(callLogs: ArrayList<MyCallLog>, itemIdx: Int)
    {
        val detailsIntent = Intent(this@MainActivity, ContactDetailsActivity::class.java).apply {
            putExtra(appConstants.CALL_LOG_KEY, callLogs[itemIdx])
            val history : Array<String> = mainViewModel.getCallLogs().value!![callLogs[itemIdx].contactNumber]!!.toTypedArray()
            putExtra(appConstants.CONTACT_HISTORY_KEY, history)
        }
        startActivity(detailsIntent)
    }
}