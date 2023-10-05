package com.spa.pin.up.up.off

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.webkit.ValueCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.spa.pin.up.off.databinding.ActivityMainBinding
import com.spa.pin.up.up.off.screens.PreLoaderActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listener: ValueCallback<Array<Uri>>? = null

    private val content = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        listener?.onReceiveValue(it.toTypedArray())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this

        startActivity(Intent(this, PreLoaderActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    private fun pickMedia(listener: ValueCallback<Array<Uri>>?) {
        this.listener = listener
        content.launch("image/*")
    }

    companion object {
        private var instance: MainActivity? = null

        fun pickMedia(listener: ValueCallback<Array<Uri>>?): Boolean {
            return instance?.let {
                pickMedia(listener)
                true
            } ?: false
        }
    }
}