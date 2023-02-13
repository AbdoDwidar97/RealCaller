package me.dwidar.realcaller.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.dwidar.realcaller.databinding.ActivityContactDetailsBinding

class ContactDetailsActivity : AppCompatActivity()
{
    lateinit var binding: ActivityContactDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}