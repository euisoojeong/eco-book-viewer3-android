package com.example.eco_ebook_viewer

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.webkit.JavascriptInterface
import android.widget.Toast

class JavascriptBridge(private val activity: Activity) {

    @JavascriptInterface
    fun close() {
        activity.finish()
    }
}