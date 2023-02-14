package me.dwidar.realcaller.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import me.dwidar.realcaller.databinding.ActivityContactDetailsBinding
import me.dwidar.realcaller.model.components.MyCallLog
import me.dwidar.realcaller.viewModel.ContactDetailsViewModel
import me.dwidar.realcaller.viewModel.MainViewModel

class ContactDetailsActivity : AppCompatActivity()
{
    lateinit var binding: ActivityContactDetailsBinding
    private lateinit var mainViewModel : MainViewModel
    private lateinit var contactDetailsViewModel: ContactDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        contactDetailsViewModel = ViewModelProvider(this)[ContactDetailsViewModel::class.java]

        binding.btnBack.setOnClickListener {
            finish()
        }

        contactDetailsViewModel.getSelectedContact().observe(this){
            Toast.makeText(this, "Contact Name : ${it.contactName}", Toast.LENGTH_SHORT).show()
            initActivity(it)
        }
    }

    private fun initActivity(myCallLog: MyCallLog)
    {
        binding.txtContactName.text = myCallLog.contactName
        binding.txtPhoneNumber.text = myCallLog.contactNumber

        Toast.makeText(this, "55555555555", Toast.LENGTH_SHORT).show()
        var myHistory = mainViewModel.getCallLogs().value!![myCallLog.contactNumber]
        val callHistoryAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myHistory!!.toList().subList(0, contactDetailsViewModel.CALL_LOGS_LIST_LENGTH))

        binding.lstCallLogs.adapter = callHistoryAdapter
    }
}