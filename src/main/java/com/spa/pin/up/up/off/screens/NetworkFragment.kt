package com.spa.pin.up.up.off.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.spa.pin.up.off.databinding.FragmentNetworkBinding
import com.spa.pin.up.up.off.WebViewActivity
import com.spa.pin.up.up.off.common.Configurator
import com.spa.pin.up.up.off.common.network.NetworkChrome
import com.spa.pin.up.up.off.common.network.NetworkClient

class NetworkFragment : Fragment() {

    private var _binding: FragmentNetworkBinding? = null
    private val binding get() = _binding!!

    private var configurator: Configurator? = null

    private lateinit var offer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNetworkBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with (binding) {
            configurator = Configurator(
                main = webView,
                client = NetworkClient(),
                chrome = NetworkChrome()
            )
            offer = WebViewActivity.urlK

            configurator?.configure(webView)
            Log.d("dataFlow2", offer)
            webView.loadUrl(offer)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    configurator?.goBack()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_OFFER = "offer"
    }
}