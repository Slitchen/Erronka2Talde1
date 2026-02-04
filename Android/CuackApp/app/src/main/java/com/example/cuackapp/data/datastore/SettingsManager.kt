// kotlin
package com.example.cuackapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    // KEYS PARA GESTIONAR DATASTORE
    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode") // modo oscuro
        private val USERNAME_KEY = stringPreferencesKey("username") // remember me
        private val PASSWORD_KEY = stringPreferencesKey("password") // remember me
    }

    //FLOW = un camino a las variables sin acceder a ellas, vaya que les echamos un vistazo
    val darkModeFlow: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[DARK_MODE_KEY] ?: false }
    val usernameFlow: Flow<String> = dataStore.data
        .map { prefs -> prefs[USERNAME_KEY] ?: "" }
    val passwordFlow: Flow<String> = dataStore.data
        .map { prefs -> prefs[PASSWORD_KEY] ?: "" }


    // FUNCIONES DE SETTEO

    suspend fun setDarkMode(isEnabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = isEnabled
        }
    }
    suspend fun setUserAndPassword(username: String, password: String) {
        dataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
            prefs[PASSWORD_KEY] = password
        }
    }
}
