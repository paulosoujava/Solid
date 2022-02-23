package com.paulo.loginapplication.usecase

import com.paulo.loginapplication.model.db.dao.UserDao
import com.paulo.loginapplication.model.dto.UserDto
import timber.log.Timber
import javax.inject.Inject

class GetUserByEmailUseCase @Inject constructor(private val userDao: UserDao) {

    suspend operator fun invoke(email: String): UserDto {
        Timber.d("invoke: $email")
        return userDao.findByEmail(email)
    }
}