package com.paulo.loginapplication.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paulo.loginapplication.model.db.dao.UserDao
import com.paulo.loginapplication.model.dto.UserDto


@Database(
    entities = [
        UserDto::class
    ],
    version = 1
)
abstract class ApplicationDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}