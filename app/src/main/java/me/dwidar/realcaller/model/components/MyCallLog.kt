package me.dwidar.realcaller.model.components

import java.io.Serializable

data class MyCallLog (
        var contactName : String,
        var contactNumber : String,
        var lastCallDate : String,
        var callLogType : String
    ) : Serializable