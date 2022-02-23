package com.paulo.loginapplication.util

import androidx.datastore.preferences.core.stringPreferencesKey

const val  DATABASE_NAME = "datastore"
const val DATASTORE_LOGGED_IN_EMAIL = "logged_in_email"

// to annotate a class to not have any getters or setters generated for it
@JvmField
val DATASTORE_LOGGED_IN_EMAIL_KEY = stringPreferencesKey(DATASTORE_LOGGED_IN_EMAIL)