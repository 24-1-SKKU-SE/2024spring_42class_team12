package com.skku.fixskkufront

import android.app.Application

class MyApplication : Application() {
    var token: String? = "token1"

    override fun onCreate() {
        super.onCreate()
    }
}
