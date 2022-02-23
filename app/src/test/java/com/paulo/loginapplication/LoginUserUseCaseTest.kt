package com.paulo.loginapplication

import com.paulo.loginapplication.model.dto.UserDto
import com.paulo.loginapplication.usecase.AddLoggedInEmailToDatastoreUseCase
import com.paulo.loginapplication.usecase.GetUserByEmailUseCase
import com.paulo.loginapplication.usecase.LoginUserUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class LoginUserUseCaseTest {

    private lateinit var getUserByEmailUseCase: GetUserByEmailUseCase
    private lateinit var addLoggedInEmailToDatastoreUseCase: AddLoggedInEmailToDatastoreUseCase

    private fun setupUseCase(
        getUserByEmailUseCase: GetUserByEmailUseCase = mock(GetUserByEmailUseCase::class.java),
        addLoggedInEmailToDatastoreUseCase: AddLoggedInEmailToDatastoreUseCase = mock(
            AddLoggedInEmailToDatastoreUseCase::class.java
        )
    ): LoginUserUseCase {
        this.getUserByEmailUseCase = getUserByEmailUseCase
        this.addLoggedInEmailToDatastoreUseCase = addLoggedInEmailToDatastoreUseCase
        return LoginUserUseCase(this.getUserByEmailUseCase, this.addLoggedInEmailToDatastoreUseCase)
    }

    @Test
    fun userNotFound() {
        val email = "email"
        val password = "password"

        val usecase = setupUseCase(getUserByEmailUseCase = mock(
            GetUserByEmailUseCase::class.java
        ) {
            throw Exception("No user found")
        })

        runBlockingTest {
            val result = usecase(email, password)
            verify(getUserByEmailUseCase, atLeastOnce()).invoke(email)
            assertEquals(LoginUserUseCase.Result.Failure, result)
        }
    }

    @Test
    fun passwordsDoNotMatch(){
        val email = "email"
        val dbPassword = "user's actual password"
        val methodInputPassword = "password"

        val usecase = setupUseCase(getUserByEmailUseCase = mock(
            GetUserByEmailUseCase::class.java
        ) {
            UserDto(0, "email", dbPassword )
        })

        runBlockingTest {
            val result = usecase(email, methodInputPassword)
            verify(getUserByEmailUseCase, atLeastOnce()).invoke(email)
            assertEquals(LoginUserUseCase.Result.Failure, result)
        }
    }

    @Test
    fun success(){
        val email = "email"
        val methodInputPassword = "password"

        val usecase = setupUseCase(
            getUserByEmailUseCase = mock(
                GetUserByEmailUseCase::class.java
            ) {
                UserDto(0, "email", methodInputPassword )
            })

        runBlockingTest {
            val result = usecase(email, methodInputPassword)
            verify(getUserByEmailUseCase, atLeastOnce()).invoke(email)
            verify(addLoggedInEmailToDatastoreUseCase, atLeastOnce()).invoke(email)
            assertEquals(LoginUserUseCase.Result.Success, result)
        }
    }
}