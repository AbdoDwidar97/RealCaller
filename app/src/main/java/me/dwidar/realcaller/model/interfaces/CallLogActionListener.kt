package me.dwidar.realcaller.model.interfaces

interface CallLogActionListener
{
    fun onCallLogItemClick(phoneNumber : String)
    fun onCallLogContactDetailsClick(itemIdx : Int)
}