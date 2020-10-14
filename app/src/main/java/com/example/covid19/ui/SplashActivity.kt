package com.example.covid19.ui

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import com.example.covid19.R
import com.example.covid19.helper.Functions
import com.example.covid19.helper.PrefUtil
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashActivity : BaseActivity() {
    var mDelayHandler: Handler? = null
    val SPLASH_DELAY: Long = 2000
    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing()) {
            if (PrefUtil.isAlreadyExisting(this)) {
                if (PrefUtil.isUserLoggedIn(this@SplashActivity)) {
                    MainActivity.launchActivity(this@SplashActivity)
                    finishAffinity()
                } else {
                    SocialLoginActivity.launchActivity(this@SplashActivity)
                    finishAffinity()
                }
            } else {
                PrefUtil.setUser(this, true)
                WalkthroughActivity.launchActivity(this@SplashActivity)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Functions.hideStatusBar(this)
        setContentView(R.layout.activity_splash)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setShowBackMessage(false)
        initViews()
        printHashKey(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }

    private fun initViews() {
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    fun printHashKey(pContext: Context) {
        try {
            val info: PackageInfo = pContext.getPackageManager()
                .getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i("TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("TAG", "printHashKey()", e)
        }
    }
}