package com.pedroid.qrcodecompose.androidapp.features.settings.data

import androidx.datastore.core.DataStore
import com.pedroid.qrcodecompose.androidapp.features.settings.data.proto.HistorySaveProto
import com.pedroid.qrcodecompose.androidapp.features.settings.data.proto.OpenUrlProto
import com.pedroid.qrcodecompose.androidapp.features.settings.data.proto.SettingsProto
import com.pedroid.qrcodecompose.androidapp.features.settings.data.proto.copy
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.FullSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GeneralSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GenerateSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HistorySavePreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.ScanSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QRCodeSettingsRepository
    @Inject
    constructor(
        private val settingsDataStore: DataStore<SettingsProto>,
    ) : SettingsRepository {
        override fun getFullSettings(): Flow<FullSettings> = settingsDataStore.data.map { it.toDomainSettings() }

        private fun SettingsProto.toDomainSettings(): FullSettings =
            FullSettings(
                general =
                    GeneralSettings(
                        openUrlPreferences = this.openUrlsIn.toDomainPreferences(),
                    ),
                scan =
                    ScanSettings(
                        hapticFeedback = this.scanHapticFeedback,
                        historySave = this.scanSaveType.toDomainPreferences(),
                    ),
                generate =
                    GenerateSettings(
                        historySave = this.generateSaveType.toDomainPreferences(),
                    ),
            )

        private fun OpenUrlProto?.toDomainPreferences(): OpenUrlPreferences =
            when (this) {
                OpenUrlProto.CUSTOM_TAB_INSIDE_APP -> OpenUrlPreferences.IN_CUSTOM_TAB
                OpenUrlProto.DEFAULT_BROWSER_APP -> OpenUrlPreferences.IN_BROWSER
                else -> OpenUrlPreferences.DEFAULT
            }

        private fun HistorySaveProto?.toDomainPreferences(): HistorySavePreferences =
            when (this) {
                HistorySaveProto.NEVER_SAVE -> HistorySavePreferences.NEVER_SAVE
                HistorySaveProto.DEFAULT_SAVE_BY_USER_ACTION -> HistorySavePreferences.UPON_USER_ACTION
                else -> HistorySavePreferences.DEFAULT
            }

        override suspend fun setOpenUrlPreferences(preferences: OpenUrlPreferences) {
            settingsDataStore.updateData {
                it.copy {
                    this.openUrlsIn = preferences.toDataStoreProto()
                }
            }
        }

        override suspend fun setScanHapticFeedback(hapticFeedbackOn: Boolean) {
            settingsDataStore.updateData {
                it.copy {
                    this.scanHapticFeedback = hapticFeedbackOn
                }
            }
        }

        override suspend fun toggleScanHapticFeedback() {
            settingsDataStore.updateData {
                it.copy {
                    this.scanHapticFeedback = !this.scanHapticFeedback
                }
            }
        }

        override suspend fun setScanHistorySavePreferences(preferences: HistorySavePreferences) {
            settingsDataStore.updateData {
                it.copy {
                    this.scanSaveType = preferences.toDataStoreProto()
                }
            }
        }

        override suspend fun setGenerateHistorySavePreferences(preferences: HistorySavePreferences) {
            settingsDataStore.updateData {
                it.copy {
                    this.generateSaveType = preferences.toDataStoreProto()
                }
            }
        }

        private fun OpenUrlPreferences.toDataStoreProto(): OpenUrlProto =
            when (this) {
                OpenUrlPreferences.IN_CUSTOM_TAB -> OpenUrlProto.CUSTOM_TAB_INSIDE_APP
                else -> OpenUrlProto.DEFAULT_BROWSER_APP
            }

        private fun HistorySavePreferences.toDataStoreProto(): HistorySaveProto =
            when (this) {
                HistorySavePreferences.NEVER_SAVE -> HistorySaveProto.NEVER_SAVE
                else -> HistorySaveProto.DEFAULT_SAVE_BY_USER_ACTION
            }
    }
