package com.example.covid19.helper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.style.ReplacementSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.example.covid19.R
import com.example.covid19.ui.BaseActivity
import de.hdodenhof.circleimageview.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

object Functions {

    val EMAIL_PATTERN: String =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    fun emailValidation(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun passwordValidation(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun getRegularFont(_context: Context): Typeface {
        return Typeface.createFromAsset(_context.assets, "fonts/OpenSans-Regular_0.ttf")
    }

    fun getBoldFont(_context: Context): Typeface {
        return Typeface.createFromAsset(_context.assets, "fonts/OpenSans-Semibold_0.ttf")
    }

    open fun fireIntent(context: BaseActivity, uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    }

    fun fireIntent(
        baseActivity: BaseActivity,
        intent: Intent,
        isNewActivity: Boolean,
        isFinished: Boolean
    ) {
        if (isFinished)
            baseActivity.finish()
        baseActivity.startActivity(intent)
        if (!isNewActivity) {
            baseActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } else {
            baseActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    fun fireIntent(
        baseActivity: BaseActivity,
        cls: Class<*>,
        isNewActivity: Boolean,
        isFinished: Boolean
    ) {
        if (isFinished)
            baseActivity.finish()
        val i = Intent(baseActivity, cls)
        baseActivity.startActivity(i)
        if (!isNewActivity) {
            baseActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } else {
            baseActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    open fun fireIntent(baseActivity: BaseActivity, isNewActivity: Boolean) {
        baseActivity.finish()
        if (!isNewActivity) {
            baseActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } else {
            baseActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    fun fireIntentWithClearFlag(context: BaseActivity, intent: Intent, isNewActivity: Boolean) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        if (!isNewActivity) {
            context.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } else {
            context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    fun checkRotationFromCamera(bitmap: Bitmap, rotate: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(rotate.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun getImageOrientation(imagePath: String?): Int {
        var rotate = 0
        try {
            val exif = imagePath?.let { ExifInterface(it) }
            val orientation: Int = exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotate
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun hideStatusBar(activity: BaseActivity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    class DrawableSpan(private val drawable: Drawable) : ReplacementSpan() {
        private val padding: Rect = Rect()
        init {
            drawable.getPadding(padding)
        }
        override fun draw(
            canvas: Canvas,
            text: CharSequence,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            val rect =
                RectF(x, top.toFloat(), x + measureText(paint, text, start, end), bottom.toFloat())
            drawable.setBounds(
                rect.left.toInt() - padding.left,
                rect.top.toInt() - padding.top,
                rect.right.toInt() + padding.right,
                rect.bottom.toInt() + padding.bottom
            )
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
            drawable.draw(canvas)
        }

        override fun getSize(
            paint: Paint,
            text: CharSequence,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int =
            Math.round(paint.measureText(text, start, end))
        private fun measureText(paint: Paint, text: CharSequence, start: Int, end: Int): Float =
            paint.measureText(text, start, end)
    }

    fun hideKeyPad(context: Context, view: View) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }


    fun getDeviceName(): String {
        return Build.MODEL + " " + Build.VERSION.RELEASE
    }

    fun getDeviceOS(): String {
        return Build.VERSION.SDK_INT.toString()
    }

    fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getDeviceType(): String {
        return "android"
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT)
    }


    interface DialogOptionsSelectedListener {
        fun onSelect(isYes: Boolean)
    }

    fun showToast(activity: BaseActivity, message: String, type: Int) {
        val mdToast = MDToast.makeText(activity, message, MDToast.LENGTH_SHORT, type)
        mdToast.show()
    }

    fun showAlertDialogWithTwoOption(
        mContext: Context,
        positiveText: String,
        negativeText: String,
        message: String,
        dialogOptionsSelectedListener: DialogOptionsSelectedListener?
    ) {
        val builder = AlertDialog.Builder(mContext)
        builder.setMessage(message)
            .setCancelable(true)
        if (positiveText.trim { it <= ' ' }.length > 0) {
            builder.setPositiveButton(positiveText) { dialog, which ->
                if (dialogOptionsSelectedListener != null)
                    dialogOptionsSelectedListener!!.onSelect(true)
                dialog.dismiss()
            }
        }
        if (negativeText.trim { it <= ' ' }.length > 0) {
            builder.setNegativeButton(negativeText) { dialog, which ->
                if (dialogOptionsSelectedListener != null)
                    dialogOptionsSelectedListener!!.onSelect(false)
                dialog.dismiss()
            }
        }
        val alert = builder.create()
        alert.setCanceledOnTouchOutside(false)
        alert.setCancelable(false)
        alert.show()
    }
}

