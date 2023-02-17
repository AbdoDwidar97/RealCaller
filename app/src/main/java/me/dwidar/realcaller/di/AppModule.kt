package me.dwidar.realcaller.di

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import me.dwidar.realcaller.MyApp
import me.dwidar.realcaller.model.components.AppConstants
import me.dwidar.realcaller.model.interfaces.OnMakePhoneCall
import me.dwidar.realcaller.model.interfaces.OnSendSMS
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context) : MyApp
    {
        return context as MyApp
    }

    @Singleton
    @Provides
    fun provideString() : String
    {
        return "Hello Hilt!"
    }

    @Singleton
    @Provides
    fun provideConstants() : AppConstants
    {
        return AppConstants()
    }

    @Singleton
    @Provides
    fun provideOnMakePhoneCallImpl() : OnMakePhoneCall
    {
        return object : OnMakePhoneCall
        {
            override fun makePhoneCall(myActivity: AppCompatActivity, phoneNumber: String)
            {
                if (ActivityCompat.checkSelfPermission(myActivity,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                {
                    val callIntent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:${phoneNumber}")
                    }
                    myActivity.startActivity(callIntent)
                }
                else {
                    ActivityCompat.requestPermissions(
                        myActivity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        1
                    )
                }
            }
        }
    }

    @Singleton
    @Provides
    fun provideOnSendSMS() : OnSendSMS
    {
        return object : OnSendSMS
        {
            @SuppressLint("QueryPermissionsNeeded")
            override fun sendSMS(myActivity: AppCompatActivity, phoneNumber: String)
            {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("sms:${phoneNumber}")
                }

                myActivity.startActivity(intent)
            }
        }
    }
}