package id.ac.itera.choirunnisasy.myprofile

import kotlinx.coroutines.flow.Flow

expect class NetworkMonitor {
    fun isConnected(): Boolean
    fun observeConnectivity(): Flow<Boolean>
}
