package com.spa.pin.up.up.off

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.spa.pin.up.off.databinding.ActivityWebViewBinding


class WebViewActivity : AppCompatActivity() {


    companion object {
        var urlK: String = ""
    }

    private val binding by lazy  {
        ActivityWebViewBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        urlK = intent.extras?.getString("url").toString()

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onStart() {
        super.onStart()


    }


}