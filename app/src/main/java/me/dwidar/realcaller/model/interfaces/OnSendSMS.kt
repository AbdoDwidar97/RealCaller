package me.dwidar.realcaller.model.interfaces

import androidx.appcompat.app.AppCompatActivity

interface OnSendSMS
{
    fun sendSMS(myActivity: AppCompatActivity, phoneNumber: String)
}