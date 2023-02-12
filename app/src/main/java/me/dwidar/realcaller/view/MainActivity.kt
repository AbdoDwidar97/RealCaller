package me.dwidar.realcaller.view

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import me.dwidar.realcaller.databinding.ActivityMainBinding
import me.dwidar.realcaller.databinding.MainActionBarBinding
import me.dwidar.realcaller.model.adapters.CallLogsAdapter
import me.dwidar.realcaller.viewModel.MainViewModel

class MainActivity : AppCompatActivity()
{
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var customActionBarBinding: MainActionBarBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var callLogsAdapter : CallLogsAdapter

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

        mainViewModel.getCallLogsNumbers().observe(this){
            callLogsAdapter = CallLogsAdapter(it)
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
    }

}