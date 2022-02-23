package com.paulo.loginapplication.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDto(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val email: String,
    val password: String
)
