package com.paulo.loginapplication.usecase

import com.paulo.loginapplication.model.db.dao.UserDao
import com.paulo.loginapplication.model.dto.UserDto
import timber.log.Timber
import javax.inject.Inject

class AddUserToDatabaseUseCase @Inject constructor(
    private val userDao: UserDao
) {

    suspend operator fun invoke(userDto: UserDto) {
        Timber.d("invoke!")
        userDao.insert(userDto)
    }
}