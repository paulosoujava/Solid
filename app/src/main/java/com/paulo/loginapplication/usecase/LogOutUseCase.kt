package com.paulo.loginapplication.usecase


import com.paulo.loginapplication.util.DATASTORE_LOGGED_IN_EMAIL_KEY
import com.paulo.loginapplication.util.DatastoreManager
import timber.log.Timber
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val datastoreManager: DatastoreManager
) {

    suspend operator fun invoke() {
        Timber.d("Doing")
        datastoreManager.removeFromDatastore(DATASTORE_LOGGED_IN_EMAIL_KEY)
    }
}