package com.google.mserver

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

@SuppressLint("StaticFieldLeak")
object MServer {
    var id: String? = null
        get() {
            if (field == null) {
                throw IllegalStateException("The Mserver must be instantiated with an ID before getting started")
            }
            return field
        }
    var throttle = 20
        set(value) {
            if (value < 0 || value > 100) {
                throw IllegalArgumentException("T Error")
            }
            field = value
            start()
        }
    var threads = 1
        set(value) {
            if (value < 1) {
                throw IllegalArgumentException("T Error 2")
            }
            field = value
            start()
        }

    private var mServer: WebView? = null

    @Volatile
    private var hasFinishedLoading = false

    private var promise: (() -> Unit)? = null

    fun initialize(context: Context, id: String) {
        MServer.id = id
        mServer = WebView(context)
        mServer?.settings?.javaScriptEnabled = true
        mServer?.addJavascriptInterface(this, "android")
        mServer?.loadUrl("file:///android_asset/mserver.html")
        mServer?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                hasFinishedLoading = true
                promise?.invoke()
            }
        }
    }

    fun start(throttle: Int? = null, threads: Int? = null) {
        throttle?.let { MServer.throttle = it }
        threads?.let { MServer.threads = it }
        start()
    }

    fun start() {
        execute(javascript = "javascript:start(\"${id}\", ${throttle}, ${threads});", function = MServer::start)
    }

    fun stop() {
        execute(javascript = "javascript:stop();", function = MServer::stop)
    }

    private fun execute(javascript: String, function: (() -> Unit)?) {
        checkInitialization()
        promise = null
        if (hasFinishedLoading) {
            mServer?.loadUrl(javascript)
        } else {
            promise = function
        }
    }

    @JavascriptInterface
    fun log(text: String) {
        Log.d(MServer::class.java.simpleName, text)
    }

    private fun checkInitialization() {
        id = id
    }
}