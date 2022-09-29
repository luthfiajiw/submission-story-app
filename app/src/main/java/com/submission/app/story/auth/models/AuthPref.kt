package com.submission.app.story.auth.models

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPref private constructor(private val dataStore: DataStore<Preferences>) {
    fun getCredential(): Flow<LoginResult> {
        return dataStore.data.map { prefs ->
            LoginResult(
                prefs[USERID] ?: "",
                prefs[NAME] ?: "",
                prefs[TOKEN] ?: ""
            )
        }
    }

    suspend fun saveCredential(loginResult: LoginResult?) {
        dataStore.edit { prefs ->
            prefs[USERID] = loginResult?.userId ?: ""
            prefs[NAME] = loginResult?.name ?: ""
            prefs[TOKEN] = loginResult?.token ?: ""
        }
    }

    suspend fun deleteCredential(loginResult: LoginResult) {
        dataStore.edit { prefs ->
            prefs[USERID] = ""
            prefs[NAME] = ""
            prefs[TOKEN] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthPref? = null

        private val USERID = stringPreferencesKey("userId")
        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): AuthPref {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}