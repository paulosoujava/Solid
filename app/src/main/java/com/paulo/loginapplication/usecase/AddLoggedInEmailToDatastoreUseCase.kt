package com.paulo.loginapplication.usecase

import com.paulo.loginapplication.util.DATASTORE_LOGGED_IN_EMAIL_KEY
import com.paulo.loginapplication.util.DatastoreManager
import timber.log.Timber
import javax.inject.Inject

class AddLoggedInEmailToDatastoreUseCase @Inject constructor(
    private val datastoreManager: DatastoreManager
) {

    suspend operator fun invoke(email: String) {
        Timber.d("invoke: $email")
        datastoreManager.addToDatastore(DATASTORE_LOGGED_IN_EMAIL_KEY, email)
    }
}