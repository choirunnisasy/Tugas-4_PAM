package id.ac.itera.choirunnisasy.myprofile.data

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeConfig {
    LIGHT, DARK, SYSTEM
}

enum class SortOrder {
    NEWEST, OLDEST, AZ
}

@OptIn(ExperimentalSettingsApi::class)
class SettingsManager(settings: ObservableSettings) {
    private val flowSettings = settings.toFlowSettings()

    companion object {
        private const val THEME_KEY = "theme_config"
        private const val SORT_ORDER_KEY = "sort_order"
    }

    val themeConfig: Flow<ThemeConfig> = flowSettings.getStringFlow(THEME_KEY, ThemeConfig.SYSTEM.name)
        .map { ThemeConfig.valueOf(it) }

    val sortOrder: Flow<SortOrder> = flowSettings.getStringFlow(SORT_ORDER_KEY, SortOrder.NEWEST.name)
        .map { SortOrder.valueOf(it) }

    suspend fun setThemeConfig(config: ThemeConfig) {
        flowSettings.putString(THEME_KEY, config.name)
    }

    suspend fun setSortOrder(order: SortOrder) {
        flowSettings.putString(SORT_ORDER_KEY, order.name)
    }
}
