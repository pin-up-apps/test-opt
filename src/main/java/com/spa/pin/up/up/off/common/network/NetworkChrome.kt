package com.spa.pin.up.up.off.common.network

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.spa.pin.up.up.off.MainActivity

class NetworkChrome: WebChromeClient() {

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        return MainActivity.pickMedia(filePathCallback)
    }

}