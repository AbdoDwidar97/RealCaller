package me.dwidar.realcaller.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import me.dwidar.realcaller.R
import me.dwidar.realcaller.databinding.ActivityContactDetailsBinding
import me.dwidar.realcaller.extensions.serializable
import me.dwidar.realcaller.model.components.AppConstants
import me.dwidar.realcaller.model.components.MyCallLog
import me.dwidar.realcaller.model.interfaces.OnMakePhoneCall
import me.dwidar.realcaller.model.interfaces.OnSendSMS
import javax.inject.Inject

@AndroidEntryPoint
class ContactDetailsActivity : AppCompatActivity()
{
    lateinit var binding: ActivityContactDetailsBinding

    @Inject
    lateinit var constants: AppConstants
    @Inject
    lateinit var onMakePhoneCall: OnMakePhoneCall
    @Inject
    lateinit var onSendSMS: OnSendSMS

    private lateinit var myCallLog: MyCallLog
    private lateinit var contactHistory: Array<String>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActivity()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initActivity()
    {
        getInfoFromExtra()
        setClickListenerActions()
    }

    private fun getInfoFromExtra()
    {
        myCallLog = intent.serializable<MyCallLog>(constants.CALL_LOG_KEY)!!
        contactHistory = intent.getStringArrayExtra(constants.CONTACT_HISTORY_KEY) as Array<String>

        binding.txtContactName.text = myCallLog.contactName
        binding.txtPhoneNumber.text = myCallLog.contactNumber

        var callHistoryAdapter : ArrayAdapter<String>

        if (contactHistory.size < constants.RECENT_LOGS_HISTORY_LENGTH)
        {
            callHistoryAdapter = ArrayAdapter<String>(this, R.layout.call_log_history_item,
                R.id.txtHistory, contactHistory)
        }
        else
        {
            callHistoryAdapter = ArrayAdapter<String>(this, R.layout.call_log_history_item, R.id.txtHistory,
                contactHistory.copyOfRange(0, constants.RECENT_LOGS_HISTORY_LENGTH))
        }

        binding.lstCallLogs.adapter = callHistoryAdapter
    }

    private fun setClickListenerActions()
    {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCall.setOnClickListener {
            onMakePhoneCall.makePhoneCall(this, myCallLog.contactNumber)
        }

        binding.btnCallNumber.setOnClickListener {
            onMakePhoneCall.makePhoneCall(this, myCallLog.contactNumber)
        }

        binding.btnSendMessage.setOnClickListener {
            onSendSMS.sendSMS(this, myCallLog.contactNumber)
        }

        binding.btnSendMsgNumber.setOnClickListener {
            onSendSMS.sendSMS(this, myCallLog.contactNumber)
        }
    }
}