package me.dwidar.realcaller.model.interfaces

import androidx.appcompat.app.AppCompatActivity

interface OnMakePhoneCall
{
    fun makePhoneCall(myActivity: AppCompatActivity, phoneNumber : String)
}