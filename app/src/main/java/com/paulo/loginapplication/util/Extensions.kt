package com.paulo.loginapplication.util

import android.content.Context
import android.util.Patterns
import androidx.annotation.AttrRes
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.util.PatternsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import java.lang.IllegalArgumentException


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATABASE_NAME)

fun String.isValidEmail():Boolean = !isEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean = this.length > 2

fun Context.getColorByAttribute(@AttrRes attr: Int): Int{
    withStyledAttributes(
        0,
        intArrayOf(attr)){
        return getColorOrThrow(0)
    }
    throw  IllegalArgumentException()
}

