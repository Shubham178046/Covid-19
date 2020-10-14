package com.example.covid19.ui

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.covid19.R
import com.example.covid19.helper.Functions
import com.example.covid19.helper.MDToast
import com.example.covid19.helper.PrefUtil
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_item_view.*
import kotlinx.android.synthetic.main.toolbar_menu.*


class MainActivity : BaseActivity() {
    private var mLastClickTime: Long = 0
    val TAG = "MainActivity"
    var mGoogleSignInClient: GoogleSignInClient? = null
    lateinit var rxPermissions: RxPermissions
    val TELEPHONE_SCHEMA = "tel:"
    val PRESERVED_CHARACTER = "+"
    val HK_COUNTRY_CODE = "91"
    val HK_OBSERVATORY_PHONE_NUMBER = "1075"

    init {
        rxPermissions = RxPermissions(this)
    }

    companion object {
        fun launchActivity(activity: BaseActivity?) {
            if (activity != null) {
                val intent = Intent(activity, MainActivity::class.java)
                //intent.putExtra("loginwith",type)
                Functions.fireIntentWithClearFlag(activity, intent, true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(this.resources.getColor(R.color.status_color))
        }
        googleConfiguration()
        initViews()
        actionListner()
    }

    fun initViews() {
        nav_view.removeAllViews()
        var customNavView: View =
            LayoutInflater.from(this).inflate(R.layout.drawer_item_view, nav_view, false)
        nav_view.addView(customNavView)
        loadButtonUi(1)
        img_home.setImageResource(R.drawable.ic_home_red);
        txt_home.setTextColor(Color.parseColor("#df3030"));
        val locale: String = this.getResources().getConfiguration().locale.getCountry()
        txtCountryName.setText(locale)
        if (PrefUtil.getUserName(this).toString().length > 0) {
            txtUserName.setText("Hi," + PrefUtil.getUserName(this))
        }
        if (PrefUtil.getUserProfile(this).toString().length > 0) {
            Glide.with(this).load(PrefUtil.getUserProfile(this)).into(profile_image);
        }
    }

    fun googleConfiguration() {
        var gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun actionListner() {
        img_menu.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.openDrawer(GravityCompat.START)
        }

        li_stayUpdate.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.closeDrawers()
            img_home.setImageResource(R.drawable.ic_home_red);
            txt_home.setTextColor(Color.parseColor("#df3030"));
            flBottomNavigation.visibility = View.GONE
            li_report.visibility = View.GONE
            loadButtonUi(8)
        }

        li_history.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.closeDrawers()
            flBottomNavigation.visibility = View.GONE
            li_report.visibility = View.GONE
            loadButtonUi(6)
        }

        li_donate.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.closeDrawers()
            flBottomNavigation.visibility = View.GONE
            li_report.visibility = View.GONE
            loadButtonUi(7)
        }

        imgLogo.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.closeDrawers()
            flBottomNavigation.visibility = View.VISIBLE
            li_report.visibility = View.VISIBLE
            img_home.setImageResource(R.drawable.ic_home_red);
            txt_home.setTextColor(Color.parseColor("#df3030"));
            loadButtonUi(1)
        }
        li_logout.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            Functions.showAlertDialogWithTwoOption(
                this,
                "YES",
                "NO",
                "Are you sure want to logout this application?",
                object : Functions.DialogOptionsSelectedListener {
                    override fun onSelect(isYes: Boolean) {
                        val account = GoogleSignIn.getLastSignedInAccount(this@MainActivity)
                        if (isYes) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                Log.d(TAG, "FacebookLogout: " + "Call")
                                facebookLogout()
                            } else if (account != null) {
                                Log.d(TAG, "GoogleLogout: " + "Call")
                                FirebaseAuth.getInstance().signOut()
                                mGoogleSignInClient!!.signOut().addOnCompleteListener(
                                    this@MainActivity,
                                    object : OnCompleteListener<Void> {
                                        override fun onComplete(p0: Task<Void>) {
                                            PrefUtil.setLoggedIn(this@MainActivity, false)
                                            PrefUtil.setUserProfile(this@MainActivity, "")
                                            PrefUtil.setUserName(this@MainActivity, "")
                                            SocialLoginActivity.launchActivity(this@MainActivity)
                                            finishAffinity()
                                        }
                                    })
                            } else {
                                PrefUtil.setLoggedIn(this@MainActivity, false)
                                SocialLoginActivity.launchActivity(this@MainActivity)
                                finishAffinity()
                            }
                        }
                    }
                })

        }

        btnRequestCall.setOnClickListener {
            RxPermissions(this@MainActivity)
                .request(Manifest.permission.CALL_PHONE)
                .subscribe { isGranted ->
                    if (isGranted) {
                        val phoneCallUri =
                            Uri.parse(TELEPHONE_SCHEMA + HK_OBSERVATORY_PHONE_NUMBER)
                        val phoneCallIntent = Intent(Intent.ACTION_CALL).also {
                            it.setData(phoneCallUri)
                        }
                        startActivity(phoneCallIntent)
                    } else {
                        Functions.showToast(this, "Permission Not Granted", MDToast.TYPE_ERROR)
                    }
                }
        }

        li_home.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.closeDrawers()
            img_home.setImageResource(R.drawable.ic_home_red);
            img_test.setImageResource(R.drawable.ic_cough);
            img_medical.setImageResource(R.drawable.ic_doctor);
            img_kit.setImageResource(R.drawable.ic_kit_gray);
            img_report.setImageResource(R.drawable.ic_clipboard_gray);

            txt_home.setTextColor(Color.parseColor("#df3030"));
            txt_test.setTextColor(Color.parseColor("#9d9c9e"));
            txt_medical.setTextColor(Color.parseColor("#9d9c9e"));
            txt_kit.setTextColor(Color.parseColor("#9d9c9e"));
            txt_report.setTextColor(Color.parseColor("#9d9c9e"));

            li_report.setBackgroundResource(R.drawable.rectangle_red_report_border);

            txt_Emergency.setVisibility(View.INVISIBLE);
            toolbar.setBackgroundColor(Color.parseColor("#00000000"));
            loadButtonUi(1)
        }
        li_test.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.closeDrawers()
            img_home.setImageResource(R.drawable.ic_home_gray);
            img_test.setImageResource(R.drawable.ic_cough_red);
            img_medical.setImageResource(R.drawable.ic_doctor);
            img_kit.setImageResource(R.drawable.ic_kit_gray);
            img_report.setImageResource(R.drawable.ic_clipboard_gray);

            txt_home.setTextColor(Color.parseColor("#9d9c9e"));
            txt_test.setTextColor(Color.parseColor("#df3030"));
            txt_medical.setTextColor(Color.parseColor("#9d9c9e"));
            txt_kit.setTextColor(Color.parseColor("#9d9c9e"));
            txt_report.setTextColor(Color.parseColor("#9d9c9e"));
            li_report.setBackgroundResource(R.drawable.rectangle_red_report_border);

            loadButtonUi(2)
        }
        li_report.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.closeDrawers()
            img_home.setImageResource(R.drawable.ic_home_gray);
            img_test.setImageResource(R.drawable.ic_cough);
            img_medical.setImageResource(R.drawable.ic_doctor);
            img_kit.setImageResource(R.drawable.ic_kit_gray);
            img_report.setImageResource(R.drawable.ic_clipboard_white);

            txt_home.setTextColor(Color.parseColor("#9d9c9e"));
            txt_test.setTextColor(Color.parseColor("#9d9c9e"));
            txt_medical.setTextColor(Color.parseColor("#9d9c9e"));
            txt_kit.setTextColor(Color.parseColor("#9d9c9e"));
            txt_report.setTextColor(Color.parseColor("#ffffff"));

            li_report.setBackgroundResource(R.drawable.rectangle_red);

            loadButtonUi(3)
        }
        li_medical.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.closeDrawers()
            img_home.setImageResource(R.drawable.ic_home_gray);
            img_test.setImageResource(R.drawable.ic_cough);
            img_medical.setImageResource(R.drawable.ic_doctor_red);
            img_kit.setImageResource(R.drawable.ic_kit_gray);
            img_report.setImageResource(R.drawable.ic_clipboard_gray);

            txt_home.setTextColor(Color.parseColor("#9d9c9e"));
            txt_test.setTextColor(Color.parseColor("#9d9c9e"));
            txt_medical.setTextColor(Color.parseColor("#df3030"));
            txt_kit.setTextColor(Color.parseColor("#9d9c9e"));
            txt_report.setTextColor(Color.parseColor("#9d9c9e"));
            li_report.setBackgroundResource(R.drawable.rectangle_red_report_border);

            loadButtonUi(4)
        }
        li_kit.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            drawer.closeDrawers()
            img_home.setImageResource(R.drawable.ic_home_gray);
            img_test.setImageResource(R.drawable.ic_cough);
            img_medical.setImageResource(R.drawable.ic_doctor);
            img_kit.setImageResource(R.drawable.ic_kit_red);
            img_report.setImageResource(R.drawable.ic_clipboard_gray);

            txt_home.setTextColor(Color.parseColor("#9d9c9e"));
            txt_test.setTextColor(Color.parseColor("#9d9c9e"));
            txt_medical.setTextColor(Color.parseColor("#9d9c9e"));
            txt_kit.setTextColor(Color.parseColor("#df3030"));
            txt_report.setTextColor(Color.parseColor("#9d9c9e"));

            li_report.setBackgroundResource(R.drawable.rectangle_red_report_border);
            txt_Emergency.setText("Emergency Kit");

            loadButtonUi(5)
        }
    }

    private fun facebookLogout() {
        if (AccessToken.getCurrentAccessToken() != null) {
            PrefUtil.setLoggedIn(this@MainActivity, false)
            PrefUtil.setUserProfile(this, "")
            PrefUtil.setUserName(this, "")
            GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/permission/", null,
                HttpMethod.DELETE,
                GraphRequest.Callback {
                    AccessToken.setCurrentAccessToken(null)
                    LoginManager.getInstance().logOut()
                }
            ).executeAsync()
            SocialLoginActivity.launchActivity(this@MainActivity)
            finishAffinity()
        }
    }
}