package com.example.kakaooauthapipractice

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()


        KakaoSdk.init(this, "...")
    }
}