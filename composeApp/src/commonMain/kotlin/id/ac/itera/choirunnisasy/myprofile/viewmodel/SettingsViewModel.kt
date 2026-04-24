package id.ac.itera.choirunnisasy.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.choirunnisasy.myprofile.data.SettingsManager
import id.ac.itera.choirunnisasy.myprofile.data.SortOrder
import id.ac.itera.choirunnisasy.myprofile.data.ThemeConfig
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsManager: SettingsManager) : ViewModel() {

    val themeConfig: StateFlow<ThemeConfig> = settingsManager.themeConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeConfig.SYSTEM)

    val sortOrder: StateFlow<SortOrder> = settingsManager.sortOrder
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SortOrder.NEWEST)

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
}
