package me.dwidar.realcaller.view
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import me.dwidar.realcaller.databinding.ActivityMainBinding
import me.dwidar.realcaller.databinding.MainActionBarBinding
import me.dwidar.realcaller.model.adapters.CallLogsAdapter
import me.dwidar.realcaller.model.interfaces.CallLogActionListener
import me.dwidar.realcaller.viewModel.ContactDetailsViewModel
import me.dwidar.realcaller.viewModel.MainViewModel

class MainActivity : AppCompatActivity()
{
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var customActionBarBinding: MainActionBarBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var contactDetailsViewModel: ContactDetailsViewModel
    private lateinit var callLogsAdapter : CallLogsAdapter
    private var phoneInit = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        customActionBarBinding = MainActionBarBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setSupportActionBar(customActionBarBinding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        contactDetailsViewModel = ViewModelProvider(this)[ContactDetailsViewModel::class.java]

        mainViewModel.checkForCallLogsPermission(applicationContext, this)
        mainViewModel.getCallLogsFromDevice(contentResolver)

        mainViewModel.getCallLogsNumbers().observe(this)
        {
            callLogsAdapter = CallLogsAdapter(it, object : CallLogActionListener {
                override fun onCallLogItemClick(phoneNumber: String)
                {
                    makePhoneCall(phoneNumber)
                }

                override fun onCallLogContactDetailsClick(itemIdx : Int)
                {
                    contactDetailsViewModel.selectContact(mainViewModel.getCallLogsNumbers().value!![itemIdx])

                    val detailsIntent = Intent(this@MainActivity, ContactDetailsActivity::class.java)
                    startActivity(detailsIntent)
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
            makePhoneCall(phoneInit)
        }
    }

    private fun makePhoneCall(number : String)
    {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
        {
            val callIntent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:${number}")
            }
            startActivity(callIntent)
        }
        else {

            phoneInit = number
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        }
    }
}