package com.example.kakaooauthapipractice

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("callback", "로그인 실패", error)
            }
            else if (token != null) {
                Log.i("callback", "로그인 성공 ${token.accessToken}")
            }
        }


        //카카오톡 설치여부에 따른 로그인 방법이 다름.
        findViewById<ImageView>(R.id.kakao_loginbutton).setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
            updateKakaoLoginUi()
        }

        findViewById<TextView>(R.id.logout_button).setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("callback", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i("callback", "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
            updateKakaoLoginUi()
        }

        updateKakaoLoginUi()
    }

    @SuppressLint("CheckResult")
    fun updateKakaoLoginUi(){
        UserApiClient.instance.me { user, error ->
            if(user != null){
                findViewById<TextView>(R.id.email_textview).text = user.kakaoAccount?.profile?.nickname.toString()
                Glide.with(this).load(user.kakaoAccount?.profile?.profileImageUrl).circleCrop().into(findViewById(R.id.user_profile))

                findViewById<ImageView>(R.id.kakao_loginbutton).visibility = View.GONE
                findViewById<TextView>(R.id.logout_button).visibility = View.VISIBLE
            }else{
                findViewById<TextView>(R.id.email_textview).text = ""
                Glide.with(this).load(R.drawable.ic_launcher_foreground).circleCrop().into(findViewById(R.id.user_profile))


                findViewById<TextView>(R.id.logout_button).visibility = View.GONE
                findViewById<ImageView>(R.id.kakao_loginbutton).visibility = View.VISIBLE
            }
        }
    }
}