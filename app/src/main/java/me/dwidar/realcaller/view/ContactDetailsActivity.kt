package me.dwidar.realcaller.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import me.dwidar.realcaller.R
import me.dwidar.realcaller.databinding.ActivityContactDetailsBinding
import me.dwidar.realcaller.model.components.AppConstants
import me.dwidar.realcaller.model.components.MyCallLog
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class ContactDetailsActivity : AppCompatActivity()
{
    lateinit var binding: ActivityContactDetailsBinding

    @Inject
    lateinit var constants: AppConstants

    private lateinit var myCallLog: MyCallLog
    private lateinit var contactHistory: Array<String>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        initActivity()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initActivity()
    {
        myCallLog = intent.serializable<MyCallLog>(constants.CALL_LOG_KEY)!!
        contactHistory = intent.getStringArrayExtra(constants.CONTACT_HISTORY_KEY) as Array<String>

        binding.txtContactName.text = myCallLog.contactName
        binding.txtPhoneNumber.text = myCallLog.contactNumber

        val callHistoryAdapter =
            ArrayAdapter<String>(this, R.layout.call_log_history_item, R.id.txtHistory, contactHistory.copyOfRange(0, constants.RECENT_LOGS_HISTORY_LENGTH))

        binding.lstCallLogs.adapter = callHistoryAdapter
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}