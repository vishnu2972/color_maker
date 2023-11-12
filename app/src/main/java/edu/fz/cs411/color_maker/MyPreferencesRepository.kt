package edu.fz.cs411.color_maker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PreferencesRepository private constructor(private val dataStore: DataStore<Preferences>) {

    private val REDSWITCH = booleanPreferencesKey("redSwitch")
    private val GREENSWITCH = booleanPreferencesKey("greenSwitch")
    private val BLUESWITCH = booleanPreferencesKey("blueSwitch")
    private val REDSEEKBAR = intPreferencesKey("redSeekBar")
    private val GREENSEEKBAR = intPreferencesKey("greenSeekBar")
    private val BLUESEEKBAR = intPreferencesKey("blueSeekBar")


    val redSwitchState: Flow<Boolean> = this.dataStore.data.map { prefs ->
        prefs[REDSWITCH] ?: false
    }.distinctUntilChanged()

    val greenSwitchState: Flow<Boolean> = this.dataStore.data.map { prefs ->
        prefs[GREENSWITCH] ?: false
    }.distinctUntilChanged()

    val blueSwitchState: Flow<Boolean> = this.dataStore.data.map { prefs ->
        prefs[BLUESWITCH] ?: false
    }.distinctUntilChanged()

    val redSeekBarValue: Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[REDSEEKBAR] ?: 0
    }.distinctUntilChanged()

    val greenSeekBarValue: Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[GREENSEEKBAR] ?: 0
    }.distinctUntilChanged()

    val blueSeekBarValue: Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[BLUESEEKBAR] ?: 0
    }.distinctUntilChanged()

    private suspend fun saveRedSeekBarState(key: Preferences.Key<Int>, value: Int) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    private suspend fun saveGreenSeekBarState(key: Preferences.Key<Int>, value: Int) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    private suspend fun saveBlueSeekBarState(key: Preferences.Key<Int>, value: Int) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    private suspend fun saveSwitchState(key: Preferences.Key<Boolean>, value: Boolean) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun saveRedSeekBarValue(value: Int) {
        saveRedSeekBarState(REDSEEKBAR, value)
    }
    suspend fun saveGreenSeekBarValue(value: Int) {
        saveGreenSeekBarState(GREENSEEKBAR, value)
    }
    suspend fun saveBlueSeekBarValue(value: Int) {
        saveBlueSeekBarState(BLUESEEKBAR, value)
    }
    suspend fun saveRedSwitchState(value: Boolean) {
        saveSwitchState(REDSWITCH, value)
    }
    suspend fun saveGreenSwitchState(value: Boolean) {
        saveSwitchState(GREENSWITCH, value)
    }
    suspend fun saveBlueSwitchState(value: Boolean) {
        saveSwitchState(BLUESWITCH, value)
    }

    companion object {
        private const val PREFERENCES_DATA_FILE_NAME = "settings"
        private var INSTANCE: PreferencesRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile(PREFERENCES_DATA_FILE_NAME)
                }
                INSTANCE = PreferencesRepository(dataStore)
            }
        }
        fun getRepository(): PreferencesRepository {
            return INSTANCE ?: throw IllegalStateException("AppPreferencesRepository not initialized yet")
        }
    }
}
