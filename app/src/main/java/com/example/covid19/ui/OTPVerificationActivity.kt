package com.example.covid19.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.covid19.R
import com.example.covid19.helper.Functions
import com.example.covid19.helper.MDToast
import com.example.covid19.helper.PrefUtil
import com.example.covid19.helper.SetOnClickListner
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_o_t_p_verification.*
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit


class OTPVerificationActivity : BaseActivity()  {
    private var editTexts: Array<EditText>? = null
    var mAuth: FirebaseAuth? = null
    var verificationId: String = ""
    var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    companion object {
        fun launchActivity(activity: BaseActivity?, mobileNumber: String) {
            if (activity != null) {
                var intent = Intent(activity, OTPVerificationActivity::class.java)
                intent.putExtra("mobileNumber", mobileNumber)
                Functions.fireIntent(activity, intent, true, false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_t_p_verification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(this.resources.getColor(R.color.status_color))
        }
        mAuth = FirebaseAuth.getInstance()
        initViews()
        actionListner()
        editTexts = arrayOf(et1, et2, et3, et4, et5, et6)
        et1.addTextChangedListener(PinTextWatcher(0))
        et2.addTextChangedListener(PinTextWatcher(1))
        et3.addTextChangedListener(PinTextWatcher(2))
        et4.addTextChangedListener(PinTextWatcher(3))
        et5.addTextChangedListener(PinTextWatcher(4))
        et6.addTextChangedListener(PinTextWatcher(5))


        et1.setOnKeyListener(PinOnKeyListener(0))
        et2.setOnKeyListener(PinOnKeyListener(1))
        et3.setOnKeyListener(PinOnKeyListener(2))
        et4.setOnKeyListener(PinOnKeyListener(3))
        et5.setOnKeyListener(PinOnKeyListener(4))
        et6.setOnKeyListener(PinOnKeyListener(5))
    }

    private fun actionListner() {
        txt_Next.setOnClickListener {
            if(edtUserName.text.length == 0 || edtUserName.text.isNullOrEmpty())
            {
                edtUserName.requestFocus()
                edtUserName.error = "Fiels is Required"
            }
            else
            {
                PrefUtil.setLoggedIn(this, true)
                Functions.showToast(this, "Login Successfull", MDToast.TYPE_SUCCESS)
                MainActivity.launchActivity(this)
            }
        }
    }

    fun initViews() {
        if (intent.getStringExtra("mobileNumber")!!.length > 0) {
            txtOtpMobileNumber.setText(intent.getStringExtra("mobileNumber"))
            requestOtp(intent.getStringExtra("mobileNumber").toString())
        }
    }
    private fun requestOtp(phonenum: String) {
        Log.d("TAG", "requestOtp: "+"Call")
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonenum,
            60L,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificationId = p0
                    forceResendingToken = p1
                }

                override fun onCodeAutoRetrievalTimeOut(p0: String) {
                    super.onCodeAutoRetrievalTimeOut(p0)
                }

                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d("TAG", "onVerificationCompleted: "+"Verified")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d("TAG", "onVerificationFailed: "+p0.message.toString())
                }

            })
    }
    inner open class PinTextWatcher : TextWatcher{
        private var currentIndex = 0
        private var isFirst = false
        private var isLast: Boolean = false
        private var newTypedString = ""

        constructor(currentIndex: Int) {
            this.currentIndex = currentIndex
            if (currentIndex == 0) isFirst =
                true else if (currentIndex == editTexts!!.size - 1) isLast = true
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            newTypedString = p0!!.subSequence(p1, p1 + p3).toString().trim();
        }

        override fun afterTextChanged(p0: Editable?) {
            var text = newTypedString
            if (!p0.toString().isEmpty()) {
                checkABC()
            } else {
                //grey
                txt_Next.setBackgroundResource(R.drawable.rectangle_gray_border);
                txt_Next.setText("Next");
                txt_edit.setVisibility(View.VISIBLE);
                txt_Next.setTextColor(Color.parseColor("#9d9c9e"));
                txt_Auto.setVisibility(View.VISIBLE);
                li_success.setVisibility(View.GONE);
            }
            if (text.length > 1) {
                text = (text.get(0).toString());
            }


            editTexts?.get(currentIndex)!!.removeTextChangedListener(this);
            editTexts?.get(currentIndex)!!.setText(text);
            editTexts?.get(currentIndex)!!.setSelection(text.length);
            editTexts?.get(currentIndex)!!.addTextChangedListener(this);

            if (text.length == 1) {
                moveToNext();
            } else if (text.length == 0)
                moveToPrevious();
        }

        private fun moveToNext() {
            if (!isLast) editTexts!![currentIndex + 1].requestFocus()
            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts!![currentIndex].clearFocus()
                hideKeyboard()
            }
        }

        private fun moveToPrevious() {
            if (!isFirst) editTexts!![currentIndex - 1].requestFocus()
        }


        private fun isAllEditTextsFilled(): Boolean {
            for (editText in editTexts!!) if (editText.text.toString()
                    .trim { it <= ' ' }.length == 0
            ) return false
            var stringBuilder = StringBuilder()
            for(i in editTexts!!)
            {
                stringBuilder.append(i.text.toString())
            }
            Log.d("TAG", "isAllEditTextsFilled: "+stringBuilder)
            Functions.hideKeyPad(this@OTPVerificationActivity,llOtp)
            verifyCode(stringBuilder.toString())
            return true
        }
        private fun hideKeyboard() {
            if (currentFocus != null) {
                val inputMethodManager: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
        }
        private fun verifyCode(code: String) {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            signInWithCredential(credential)
        }
        private fun signInWithCredential(credential: PhoneAuthCredential) {
            mAuth!!.signInWithCredential(credential).addOnCompleteListener(object :
                OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        txt_Next.isEnabled = true
                        txt_Next.setBackgroundResource(R.drawable.rectangle_red)
                        txt_Next.setTextColor(Color.parseColor("#ffffff"))
                        txt_Next.text = "Continue"
                        txt_edit.visibility = View.INVISIBLE
                        txt_Auto.visibility = View.GONE
                        li_success.setVisibility(View.VISIBLE)
                    } else {
                        Functions.showToast(this@OTPVerificationActivity, "Invalid Otp",MDToast.TYPE_ERROR)
                    }
                }

            })
        }
        private fun checkABC() {
            if (et1.text.toString().isEmpty()) {
                //grey
                txt_Next.setBackgroundColor(Color.parseColor("#C7CACF"))
            } else {
                if (et2.text.toString().isEmpty()) {
                    //grey
                    txt_Next.setBackgroundColor(Color.parseColor("#C7CACF"))
                } else {
                    if (et3.text.toString().isEmpty()) {
                        //grey
                        txt_Next.setBackgroundColor(Color.parseColor("#C7CACF"))
                    } else {
                        if (et4.text.toString().isEmpty()) {
                            //grey
                            txt_Next.setBackgroundColor(Color.parseColor("#C7CACF"))
                            et4.isCursorVisible = false
                        } else {
                            //green
                            if (et5.text.toString().isEmpty()) {
                                //grey
                                txt_Next.setBackgroundColor(Color.parseColor("#C7CACF"))
                                et5.isCursorVisible = false
                            } else {
                                if (et6.text.toString().isEmpty()) {
                                    //grey
                                    txt_Next.setBackgroundColor(Color.parseColor("#C7CACF"))
                                    et6.isCursorVisible = false
                                } else {
                                    txt_Next.setBackgroundColor(Color.parseColor("#349d13"))
                                    et1.isCursorVisible = false
                                }
                            }
                            /*txt_Next.setBackgroundColor(Color.parseColor("#349d13"))
                            et1.isCursorVisible = false*/
                        }
                    }
                }
            }
        }


    }
    inner open class PinOnKeyListener : View.OnKeyListener {
        private var currentIndex = 0

        constructor(currentIndex: Int) {
            this.currentIndex = currentIndex
        }

        override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
            if (p1 == KeyEvent.KEYCODE_DEL && p2!!.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts!!.get(currentIndex).getText().toString()
                        .isEmpty() && currentIndex != 0
                )
                    editTexts!![currentIndex - 1].requestFocus();
            }
            return false;
        }

    }
}