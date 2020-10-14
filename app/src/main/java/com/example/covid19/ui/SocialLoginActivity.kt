package com.example.covid19.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.example.covid19.R
import com.example.covid19.helper.Functions
import com.example.covid19.helper.MDToast
import com.example.covid19.helper.PrefUtil
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_social_login.*
import java.util.*


class SocialLoginActivity : BaseActivity() {
    var callbackManager: CallbackManager? = null
    var user: FirebaseUser? = null
    private var mAuth: FirebaseAuth? = null
    private val EMAIL = "email"
    private val RC_SIGN_IN = 9001
    var mGoogleSignInClient: GoogleSignInClient? = null
    var isFrom : Int = -1
    companion object {
        fun launchActivity(activity: BaseActivity?) {
            if (activity != null) {
                val intent = Intent(activity, SocialLoginActivity::class.java)
                Functions.fireIntentWithClearFlag(activity, intent, true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(this.resources.getColor(R.color.status_color))
        }
        googleConfiguration()
        mAuth = FirebaseAuth.getInstance();

        actionListner()

    }

    fun googleConfiguration() {
        var gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun facebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d("TAG", "onSuccess: " + "Call")
                    handleFacebookAccessToken(result!!.accessToken)
                }

                override fun onCancel() {
                    Functions.showToast(
                        this@SocialLoginActivity,
                        "Login Cancel",
                        MDToast.TYPE_ERROR
                    )
                }

                override fun onError(error: FacebookException?) {
                    Functions.showToast(
                        this@SocialLoginActivity,
                        "Errow While Login",
                        MDToast.TYPE_ERROR
                    )
                }

            })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        Log.d("TAG", "handleFacebookAccessToken: " + credential)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        Functions.showToast(
                            this@SocialLoginActivity,
                            "Login Successfull",
                            MDToast.TYPE_SUCCESS
                        )
                        user = mAuth!!.getCurrentUser()!!
                        Log.d("PhotoUrl", "onComplete: " + user?.photoUrl)
                        PrefUtil.setLoggedIn(this@SocialLoginActivity, true)
                        val profile: Profile = Profile.getCurrentProfile()
                        PrefUtil.setUserName(this@SocialLoginActivity, profile.name)
                        PrefUtil.setUserProfile(
                            this@SocialLoginActivity,
                            profile.getProfilePictureUri(96, 96).toString()!!
                        )
                        MainActivity.launchActivity(this@SocialLoginActivity)
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException())
                        Functions.showToast(
                            this@SocialLoginActivity,
                            "Login Failed",
                            MDToast.TYPE_ERROR
                        )

                    }
                }
            })
    }


    fun actionListner() {
        llContinueGuest.setOnClickListener {
            LoginActivity.launchActivity(this)
        }
        txtFacebook.setOnClickListener {
            isFrom = 0
            facebookLogin()
        }

        txtGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(isFrom == 0)
        {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
        else if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Functions.showToast(
                    this@SocialLoginActivity,
                    "Google SignIn Failed",
                    MDToast.TYPE_ERROR
                )
            }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.getIdToken(), null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        val user = mAuth!!.currentUser
                        Functions.showToast(
                            this@SocialLoginActivity,
                            "Login Successfull",
                            MDToast.TYPE_SUCCESS
                        )
                        PrefUtil.setLoggedIn(this@SocialLoginActivity, true)
                        PrefUtil.setUserName(this@SocialLoginActivity, user!!.displayName!!)
                        PrefUtil.setUserProfile(
                            this@SocialLoginActivity,
                            user.photoUrl.toString()
                        )
                        MainActivity.launchActivity(this@SocialLoginActivity)
                    } else {
                        Functions.showToast(
                            this@SocialLoginActivity,
                            "Authentication Failed",
                            MDToast.TYPE_ERROR
                        )
                    }
                }
            })
    }
}