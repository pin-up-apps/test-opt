package com.spa.pin.up.up.off.screens

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.ValueCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.onesignal.OneSignal
import com.spa.pin.up.off.R
import com.spa.pin.up.up.off.WebViewActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class PreLoaderActivity : AppCompatActivity() {
    private var listener: ValueCallback<Array<Uri>>? = null

    private val content = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        listener?.onReceiveValue(it.toTypedArray())
    }

    var remoteConfig: Any? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE)

        OneSignal.initWithContext(this)
        OneSignal.setAppId("3c5c3baf-e425-4935-b308-da7dc1c3c3c6")


        setContentView(R.layout.activity_pre_loader)

        if (sharedPrefs?.getString("link", null) == null) {

            val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings =
                FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600)
                    .build()
            firebaseRemoteConfig.setConfigSettingsAsync(configSettings)


            lifecycleScope.launch(Dispatchers.IO) {
                apps().collect {

                    Log.d("dataFlow", "conversion2:  " + it.toString())

                    firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener { aVoid: Boolean? ->
                        Log.d("tags2", FirebaseRemoteConfig.getInstance().getString("url"))
                        lifecycleScope.launch(Dispatchers.Main) {
                            Log.d("dataFlow", "conversion3:  " + FirebaseRemoteConfig.getInstance()
                                .getString("url") + "?st=${it?.get("campaign_id")}")

                        startActivity(
                            Intent(this@PreLoaderActivity, WebViewActivity::class.java).putExtra(
                                "url",
                                FirebaseRemoteConfig.getInstance()
                                    .getString("url") + "?st=${it?.get("campaign_id")}"
                            )
                        )
                        }
                    }
                }
            }
        } else {
            startActivity(
                Intent(this@PreLoaderActivity, WebViewActivity::class.java).putExtra(
                    "url",
                    "${sharedPrefs?.getString("link", null)}"
                )
            )
        }


    }


//    private fun apps() = callbackFlow {
//
//        val firebaseRemoteConfig: FirebaseRemoteConfig
//        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
//        val configSettings =
//            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build()
//        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
//        firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener { aVoid: Boolean? ->
//            Log.d("tags", FirebaseRemoteConfig.getInstance().getString("url"))
//            trySend(FirebaseRemoteConfig.getInstance().getString("url"))
//        }
//
//
//        awaitClose {
//
//            cancel()
//
//        }
//    }


    private fun apps() = callbackFlow {
        AppsFlyerLib.getInstance().init(
            APPS_FLYER_DEV_KEY,
            object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                    trySend(data)
                 //   Log.d("dataFlow", "conversion:  " + data.toString())
                }

                override fun onConversionDataFail(message: String?) {
                    trySend(null)
                    Log.d("dataFlow", "conversion:  " + " null")

                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {

                }

                override fun onAttributionFailure(p0: String?) {

                }
            },
            this@PreLoaderActivity
        )
        AppsFlyerLib.getInstance().start(this@PreLoaderActivity)
        awaitClose {
            cancel()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }


    companion object {
        private const val TAG = "PreLoaderActivity"
        var sharedPrefs: SharedPreferences? = null
        val editor: SharedPreferences.Editor by lazy {
            sharedPrefs!!.edit()
        }

        private var instance: PreLoaderActivity? = null
        const val APPS_FLYER_DEV_KEY = "m5ZqwaoEQYpwAg8CXbWzKm"

        fun pickMedia(listener: ValueCallback<Array<Uri>>?): Boolean {
            return instance?.let {
                pickMedia(listener)
                true
            } ?: false
        }
    }
}