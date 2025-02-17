package com.kaushaltechnology.india.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkHelper @Inject constructor(private val context: Context) {

    // Function to check if network is connected
    fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    // Observe network status changes using channelFlow
    fun observeNetworkStatus() = channelFlow {
        // Emit the initial network status
        send(isConnected())

        // Create the network callback to listen for changes
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.e("TAG","Internet -Status true")
                // Emit true when network is available
                launch { send(true) }
               /*Coro {
                   send(true)
               }*/
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.e("TAG","Internet -Status false")
                launch { send(false) }
                // Emit false when network is lost
                //send(false)
            }
        }

        // Register the network callback to observe connectivity changes
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        // Close the flow when the NetworkCallback is no longer needed
        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}
