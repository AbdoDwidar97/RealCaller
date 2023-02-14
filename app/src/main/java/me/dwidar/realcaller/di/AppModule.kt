package me.dwidar.realcaller.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import me.dwidar.realcaller.MyApp
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

}