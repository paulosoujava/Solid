package com.paulo.loginapplication.usecase

import com.paulo.loginapplication.model.dto.UserDto
import timber.log.Timber
import javax.inject.Inject

open class RegisterUserUseCase @Inject constructor(
    private val addUserToDatabaseUseCase: AddUserToDatabaseUseCase,
    private val checkIfUserExistsUseCase: CheckIfUserExistsUseCase
) {

    open suspend operator fun invoke(email: String, password: String): Result {
        Timber.d("invoke: $email")
        val userExists = checkIfUserExistsUseCase(email)
        return if (!userExists) {
            addUserToDatabaseUseCase(UserDto(email = email, password = password))
            Timber.d("Success!")
            Result.Success
        } else {
            Timber.d("Failure!")
            Result.Failure
        }
    }

    sealed class Result {
        object Success : Result()
        object Failure : Result()
    }
}