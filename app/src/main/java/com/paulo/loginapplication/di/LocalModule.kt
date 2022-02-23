package com.paulo.loginapplication.di

import android.content.Context
import androidx.room.Room
import com.paulo.loginapplication.model.db.ApplicationDatabase
import com.paulo.loginapplication.model.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun applicationDatabase(@ApplicationContext context: Context): ApplicationDatabase{
        return Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            "user.db"
        ).build()
    }

    @Provides
    @Singleton
    fun userDao(applicationDatabase: ApplicationDatabase): UserDao {
        return applicationDatabase.userDao()
    }
}