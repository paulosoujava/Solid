package com.paulo.loginapplication

import app.cash.turbine.test
import com.paulo.loginapplication.screens.login.LoginErrorType
import com.paulo.loginapplication.screens.login.LoginState
import com.paulo.loginapplication.screens.login.LoginViewModel


import com.paulo.loginapplication.usecase.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.atLeastOnce
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var loginUserUseCase: LoginUserUseCase
    private lateinit var registerUserUseCase: RegisterUserUseCase
    private lateinit var getForgottenPasswordUseCase: GetForgottenPasswordUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    fun setupViewModel(
        loginUserUseCase: LoginUserUseCase = mock(LoginUserUseCase::class.java),
        registerUserUseCase: RegisterUserUseCase = mock(RegisterUserUseCase::class.java),
        getForgottenPasswordUseCase: GetForgottenPasswordUseCase = mock(GetForgottenPasswordUseCase::class.java)
    ): LoginViewModel {
        this.loginUserUseCase = loginUserUseCase
        this.registerUserUseCase = registerUserUseCase
        this.getForgottenPasswordUseCase = getForgottenPasswordUseCase
        return LoginViewModel(
            this.loginUserUseCase,
            this.registerUserUseCase,
            this.getForgottenPasswordUseCase
        )
    }

    @Test
    fun loginClicked_validateInputsErrors() {
        val viewModel = setupViewModel()
        val invalidUsername = ""
        val invalidPassword = ""
        runBlockingTest {
            viewModel.state.test {
                assertEquals(LoginState(), awaitItem())
                viewModel.loginClicked(invalidUsername, invalidPassword)
                verify(loginUserUseCase, never()).invoke(invalidUsername, invalidPassword)
                assertEquals(LoginState(false, false), awaitItem())
            }
        }
    }

    @Test
    fun loginClicked_inputsGood_loginError() {
        val viewModel = setupViewModel(loginUserUseCase = mock(
            LoginUserUseCase::class.java
        ) {
            LoginUserUseCase.Result.Failure
        })
        val userName = "aleks@appstand.com"
        val password = "qwerty123"
        runBlockingTest {
            viewModel.error.test {
                viewModel.loginClicked(userName, password)
                verify(loginUserUseCase, atLeastOnce()).invoke(userName, password)
                assertEquals(LoginErrorType.LOGIN, awaitItem())
            }
        }
    }

    @Test
    fun loginClicked_inputsGood_loginSuccess() {
        val viewModel = setupViewModel(loginUserUseCase = mock(
            LoginUserUseCase::class.java
        ) {
            LoginUserUseCase.Result.Success
        })
        val invalidUsername = ""
        val invalidPassword = ""
        val userName = "aleks@appstand.com"
        val password = "qwerty123"
        runBlockingTest {
            viewModel.state.test {
                assertEquals(LoginState(), awaitItem())
                viewModel.loginClicked(invalidUsername, invalidPassword)
                assertEquals(LoginState(false, false), awaitItem())
                viewModel.loginClicked(userName, password)
                assertEquals(LoginState(), awaitItem())
            }
            viewModel.error.test {
                viewModel.loginClicked(userName, password)
                expectNoEvents()
            }
            viewModel.navigateToApp.test {
                viewModel.loginClicked(userName, password)
                assertEquals(Unit, awaitItem())
            }
            verify(loginUserUseCase, atLeast(3)).invoke(userName, password)
        }
    }

    @Test
    fun signupClicked_inputsGood_signupError() {
        val viewModel = setupViewModel(registerUserUseCase = mock(
            RegisterUserUseCase::class.java
        ) {
            RegisterUserUseCase.Result.Failure
        })
        val userName = "aleks@appstand.com"
        val password = "qwerty123"
        runBlockingTest {
            viewModel.error.test {
                viewModel.signUpClicked(userName, password)
                verify(registerUserUseCase, atLeastOnce()).invoke(userName, password)
                assertEquals(LoginErrorType.SIGNUP, awaitItem())
            }
        }
    }

    @Test
    fun signUpClicked_inputsGood_registerSuccess() {
        val viewModel = setupViewModel(
            loginUserUseCase = mock(
                LoginUserUseCase::class.java
            ) {
                LoginUserUseCase.Result.Success
            },
            registerUserUseCase = mock(
                RegisterUserUseCase::class.java
            ) {
                RegisterUserUseCase.Result.Success
            })
        val invalidUsername = ""
        val invalidPassword = ""
        val userName = "aleks@appstand.com"
        val password = "qwerty123"
        runBlockingTest {
            viewModel.state.test {
                assertEquals(LoginState(), awaitItem())
                viewModel.signUpClicked(invalidUsername, invalidPassword)
                assertEquals(LoginState(false, false), awaitItem())
                viewModel.signUpClicked(userName, password)
                assertEquals(LoginState(), awaitItem())
            }
            viewModel.error.test {
                viewModel.signUpClicked(userName, password)
                expectNoEvents()
            }
            viewModel.registerSuccess.test {
                viewModel.signUpClicked(userName, password)
                assertEquals(Unit, awaitItem())
            }
            verify(registerUserUseCase, atLeast(3)).invoke(userName, password)
            verify(loginUserUseCase, atLeast(3)).invoke(userName, password)
        }
    }

    @Test
    fun forgotPasswordClicked() {
        val viewModel = setupViewModel()
        runBlockingTest {
            viewModel.bottomSheetShow.test {
                viewModel.forgotPasswordClicked()
                assertEquals(Unit, awaitItem())
            }
        }
    }

    @Test
    fun forgotPasswordSubmitClicked_success() {
        val password = "qwerty123"
        val email = "aleks@appstand.com"

        val viewModel =
            setupViewModel(
                getForgottenPasswordUseCase = mock(GetForgottenPasswordUseCase::class.java) {
                    Result.Success(password)
                }
            )
        runBlockingTest {
            viewModel.forgotPasswordGetSuccess.test {
                viewModel.forgotPasswordSubmitClicked(email)
                verify(getForgottenPasswordUseCase, atLeastOnce()).invoke(email)
                assertEquals(password, awaitItem())
            }
        }
    }

    @Test
    fun forgotPasswordSubmitClicked_failure() {
        val email = "aleks@appstand.com"

        val viewModel =
            setupViewModel(
                getForgottenPasswordUseCase = mock(GetForgottenPasswordUseCase::class.java) {
                    Result.Failure
                }
            )
        runBlockingTest {
            viewModel.error.test {
                viewModel.forgotPasswordSubmitClicked(email)
                verify(getForgottenPasswordUseCase, atLeastOnce()).invoke(email)
                assertEquals(LoginErrorType.FORGOT_PASSWORD, awaitItem())
            }
        }
    }

    @Test
    fun onRegistrationSnackbarDismissed() {
        val viewModel = setupViewModel()
        runBlockingTest {
            viewModel.navigateToApp.test {
                viewModel.onRegistrationSnackbarDismissed()
                assertEquals(Unit, awaitItem())
            }
        }
    }
}