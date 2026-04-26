package id.ac.itera.choirunnisasy.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.choirunnisasy.myprofile.data.SettingsManager
import id.ac.itera.choirunnisasy.myprofile.data.SortOrder
import id.ac.itera.choirunnisasy.myprofile.data.ThemeConfig
import id.ac.itera.choirunnisasy.myprofile.PlatformSpecificFetcher
import id.ac.itera.choirunnisasy.myprofile.BatteryInfo
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsManager: SettingsManager,
    private val fetcher: PlatformSpecificFetcher,
    val permissionsController: PermissionsController,
    private val batteryInfo: BatteryInfo
) : ViewModel() {

    val themeConfig: StateFlow<ThemeConfig> = settingsManager.themeConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeConfig.SYSTEM)

    val sortOrder: StateFlow<SortOrder> = settingsManager.sortOrder
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SortOrder.NEWEST)

    val deviceName: String = fetcher.getOsName()
    val appVersion: String = fetcher.getAppVersion()
    
    val batteryLevel: Int = batteryInfo.getBatteryLevel()
    val isCharging: Boolean = batteryInfo.isCharging()

    fun setThemeConfig(config: ThemeConfig) {
        viewModelScope.launch {
            settingsManager.setThemeConfig(config)
        }
    }

    fun setSortOrder(order: SortOrder) {
        viewModelScope.launch {
            settingsManager.setSortOrder(order)
        }
    }

    fun requestCameraPermission() {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.CAMERA)
            } catch (e: DeniedAlwaysException) {
                permissionsController.openAppSettings()
            } catch (e: DeniedException) {
                // Denied once
            } catch (e: Exception) {
                // Handle other errors
            }
        }
    }
}
