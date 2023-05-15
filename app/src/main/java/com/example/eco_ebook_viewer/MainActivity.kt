package com.example.eco_ebook_viewer

import android.app.Activity
import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private val activity: Activity by lazy { this }
    private lateinit var webView: WebView
    private var backBtnTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)

        initWebView()
    }

    override fun onStart() {
        super.onStart()
        webView.onResume()
    }

    override fun onStop() {
        webView.onPause()
        super.onStop()
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }

    private fun initWebView() {
        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
            }
            webViewClient = WebViewClient()
            webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                    AlertDialog.Builder(activity)
                        .setTitle("알림")
                        .setMessage(message ?: "")
                        .setCancelable(false)
                        .setPositiveButton("확인") { _, _ -> result?.confirm() }
                        .create()
                        .show()

                    return true
                }

                override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                    AlertDialog.Builder(activity)
                        .setTitle("알림")
                        .setMessage(message ?: "")
                        .setCancelable(false)
                        .setPositiveButton("확인") { _, _ -> result?.confirm() }
                        .setNegativeButton("취소") { _, _ -> result?.cancel() }
                        .create()
                        .show()

                    return true
                }
            }
            webView.addJavascriptInterface(JavascriptBridge(activity), "App")
            load()
        }
    }

    private fun load() {
        val url = intent.getStringExtra("openUrl") ?: BuildConfig.BASE_URL
        webView.loadUrl(url)
    }



    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val gapTime = currentTime - backBtnTime
        if (webView.canGoBack()) {
            webView.goBack()
        } else if (gapTime in 0..2000) {
            super.onBackPressed()
        } else {
            backBtnTime = currentTime
            Toast.makeText(this, "한번 더 누르면 종료 됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
