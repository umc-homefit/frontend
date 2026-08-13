package com.umc.homefit.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "homefit_prefs")

object PreferencesKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val SAVED_RECRUITMENT_IDS = stringPreferencesKey("saved_recruitment_ids")
    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    val RECRUITMENT_RECENT_SEARCHES = stringPreferencesKey("recruitment_recent_searches")
    val PRODUCT_RECENT_SEARCHES = stringPreferencesKey("product_recent_searches")
}

private const val RECENT_SEARCHES_LIMIT = 10

class UserPreferencesDataSource(
    private val context: Context
) {
    private val dataStore = context.dataStore

    val accessToken: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN]
        }

    val savedRecruitmentIds: Flow<List<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val idsString = preferences[PreferencesKeys.SAVED_RECRUITMENT_IDS] ?: ""
            if (idsString.isEmpty()) emptyList() else idsString.split(",")
        }

    val notificationsEnabled: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
        }

    val recruitmentRecentSearches: Flow<List<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.RECRUITMENT_RECENT_SEARCHES].toRecentSearchList()
        }

    val productRecentSearches: Flow<List<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.PRODUCT_RECENT_SEARCHES].toRecentSearchList()
        }

    suspend fun updateAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = token
        }
    }

    suspend fun clearAccessToken() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.ACCESS_TOKEN)
        }
    }

    suspend fun saveRecruitmentId(id: String) {
        dataStore.edit { preferences ->
            val currentIds = preferences[PreferencesKeys.SAVED_RECRUITMENT_IDS]
                ?.split(",")
                ?.filter { it.isNotEmpty() }
                ?.toMutableList() ?: mutableListOf()
            if (!currentIds.contains(id)) {
                currentIds.add(id)
                preferences[PreferencesKeys.SAVED_RECRUITMENT_IDS] = currentIds.joinToString(",")
            }
        }
    }

    suspend fun removeRecruitmentId(id: String) {
        dataStore.edit { preferences ->
            val currentIds = preferences[PreferencesKeys.SAVED_RECRUITMENT_IDS]
                ?.split(",")
                ?.filter { it.isNotEmpty() }
                ?.toMutableList() ?: mutableListOf()
            if (currentIds.contains(id)) {
                currentIds.remove(id)
                preferences[PreferencesKeys.SAVED_RECRUITMENT_IDS] = currentIds.joinToString(",")
            }
        }
    }

    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun addRecruitmentRecentSearch(keyword: String) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.RECRUITMENT_RECENT_SEARCHES].toRecentSearchList()
            preferences[PreferencesKeys.RECRUITMENT_RECENT_SEARCHES] = current.withRecentSearchAdded(keyword)
        }
    }

    suspend fun removeRecruitmentRecentSearch(keyword: String) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.RECRUITMENT_RECENT_SEARCHES].toRecentSearchList()
            preferences[PreferencesKeys.RECRUITMENT_RECENT_SEARCHES] = current.filterNot { it == keyword }.joinToString(",")
        }
    }

    suspend fun addProductRecentSearch(keyword: String) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.PRODUCT_RECENT_SEARCHES].toRecentSearchList()
            preferences[PreferencesKeys.PRODUCT_RECENT_SEARCHES] = current.withRecentSearchAdded(keyword)
        }
    }

    suspend fun removeProductRecentSearch(keyword: String) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.PRODUCT_RECENT_SEARCHES].toRecentSearchList()
            preferences[PreferencesKeys.PRODUCT_RECENT_SEARCHES] = current.filterNot { it == keyword }.joinToString(",")
        }
    }

    private fun String?.toRecentSearchList(): List<String> {
        return this?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    private fun List<String>.withRecentSearchAdded(keyword: String): String {
        val trimmedKeyword = keyword.trim()
        if (trimmedKeyword.isBlank()) return joinToString(",")

        return (listOf(trimmedKeyword) + filterNot { it == trimmedKeyword })
            .take(RECENT_SEARCHES_LIMIT)
            .joinToString(",")
    }
}
