package id.ac.itera.choirunnisasy.myprofile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class NetworkMonitor actual constructor() {
    actual fun isConnected(): Boolean = true
    actual fun observeConnectivity(): Flow<Boolean> = flowOf(true)
}
