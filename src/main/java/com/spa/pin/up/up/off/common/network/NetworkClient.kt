package com.spa.pin.up.up.off.common.network

import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.spa.pin.up.up.off.screens.PreLoaderActivity.Companion.sharedPrefs

class NetworkClient : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        CookieManager.getInstance().flush()

        when (sharedPrefs?.getString("link", null)){
            null -> {
                Log.d("dataSave", "saved url - $url")
                sharedPrefs?.edit()?.putString("link", url)?.apply()
            }
            else -> {

            }
        }

    }

}