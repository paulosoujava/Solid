package com.paulo.loginapplication.usecase

import com.paulo.loginapplication.model.domain.User
import com.paulo.loginapplication.util.DATASTORE_LOGGED_IN_EMAIL_KEY
import com.paulo.loginapplication.util.DatastoreManager
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ObserveLoggedInUserUseCase @Inject constructor(
    private val datastoreManager: DatastoreManager
) {

    operator fun invoke(): Flow<User?> {
        return datastoreManager.observeKeyValue(DATASTORE_LOGGED_IN_EMAIL_KEY).map {
            if (it != null) {
                User(it)
            } else {
                null
            }
        }
    }
}