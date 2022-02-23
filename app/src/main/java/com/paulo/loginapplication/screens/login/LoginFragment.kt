package com.paulo.loginapplication.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.paulo.loginapplication.R
import com.paulo.loginapplication.databinding.ForgotPassBinding
import com.paulo.loginapplication.databinding.FragmentLoginBinding
import com.paulo.loginapplication.util.getColorByAttribute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeViewModel()
    }

    private fun observeViewModel() {
        with(lifecycleScope) {
            viewModel.state.onEach { state ->
                Timber.d(state.toString())
                binding.usernameLayout.error = if (state.isEmailValid) {
                    null
                } else {
                    resources.getString(R.string.login_invalid_email)
                }
                binding.passwordLayout.error = if (state.isPasswordValid) {
                    null
                } else {
                    resources.getString(R.string.login_invalid_password)
                }
            }.launchIn(this)

            viewModel.error.onEach {
                Timber.d(it.toString())
                showSnackbar(
                    if (it == LoginErrorType.LOGIN) {
                        resources.getString(R.string.login_failed)
                    } else {
                        resources.getString(R.string.login_error)
                    },
                    requireContext().getColorByAttribute(androidx.appcompat.R.attr.colorError)
                )
            }.launchIn(this)

            viewModel.registerSuccess.onEach {
                Timber.d("Register success!")
                showSnackbar(
                    resources.getString(R.string.login_register_success),
                    requireContext().getColorByAttribute(androidx.appcompat.R.attr.colorPrimary),
                    object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            viewModel.onRegistrationSnackbarDismissed()
                        }
                    }
                )
            }.launchIn(this)

            viewModel.bottomSheetShow.onEach {
                Timber.d("Showing forgot password dialog")
                showForgotPasswordBottomSheetDialog()
            }.launchIn(this)

            viewModel.forgotPasswordGetSuccess.onEach {
                Timber.d("Get password success")
                showSnackbar(
                    resources.getString(R.string.login_your_password_is, it),
                    requireContext().getColorByAttribute(com.google.android.material.R.attr.colorOnPrimary)
                )
            }.launchIn(this)

            viewModel.navigateToApp.onEach {
                findNavController().navigate(R.id.toLoggedIn)
            }.launchIn(this)
        }
    }

    private fun setup() {
        binding.loginButton.setOnClickListener {
            viewModel.loginClicked(
                binding.usernameEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }

        binding.signUpButton.setOnClickListener {
            viewModel.signUpClicked(
                binding.usernameEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }

        binding.forgotPasswordView.setOnClickListener {
            viewModel.forgotPasswordClicked()
        }
    }

    private fun showSnackbar(
        message: String,
        backgroundTint: Int,
        callback: BaseTransientBottomBar.BaseCallback<Snackbar>? = null
    ) {
        val snackbar = Snackbar
            .make(
                requireContext(),
                binding.root,
                message,
                Snackbar.LENGTH_LONG
            )
            .setBackgroundTint(backgroundTint)
        if (callback != null) {
            snackbar.addCallback(callback)
        }
        snackbar.show()
    }

    private fun showForgotPasswordBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val forgotPasswordBinding = ForgotPassBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(forgotPasswordBinding.root)

        forgotPasswordBinding.submitButton.setOnClickListener {
            viewModel.forgotPasswordSubmitClicked(
                forgotPasswordBinding.usernameEditText.text.toString()
            )
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}