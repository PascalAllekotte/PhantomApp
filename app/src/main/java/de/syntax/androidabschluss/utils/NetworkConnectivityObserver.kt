package de.syntax.androidabschluss.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.telephony.TelephonyManager
import androidx.lifecycle.LiveData


//Diese Klasse erweitert LiveData um die Netzwerkverfügbarkeit auf Android-Geräten zu überwachen,
//indem sie ConnectivityManager und TelephonyManager verwendet,
//um Netzwerkänderungen zu registrieren und den Status entweder als verfügbar oder nicht verfügbar zu posten,
//abhängig von der Verfügbarkeit der SIM-Karte und der Netzwerkverbindung.

class NetworkConnectivityObserver(context: Context) : LiveData<NetworkStatus>() {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .build()

    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    private val callback = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(NetworkStatus.Available)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(NetworkStatus.Unavailable)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(NetworkStatus.Unavailable)
        }
    }

    override fun onActive() {
        super.onActive()
        if (TelephonyManager.SIM_STATE_ABSENT != telephonyManager.simState){
            postValue(NetworkStatus.Available)
        }else{
            postValue(NetworkStatus.Unavailable)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            connectivityManager.registerDefaultNetworkCallback(callback)
        }else{
            connectivityManager.registerNetworkCallback(networkRequest,callback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(callback)
    }
}