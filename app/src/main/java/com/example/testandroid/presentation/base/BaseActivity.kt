package com.example.testandroid.presentation.base

import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.paperdb.Paper
import android.app.ProgressDialog
import android.hardware.camera2.CameraCharacteristics
import android.widget.Toast
import com.example.testandroid.App.Companion.context
import com.example.testandroid.R
import com.example.testandroid.utils.LanguageSettings


//@AndroidEntryPoint
abstract class BaseActivity<D : ViewDataBinding> : AppCompatActivity() {
    private var dataBinding: D? = null
    private val TAG = "BaseActivity"
    var mInternetDialog: AlertDialog? = null
    private var WIFI_ENABLE_REQUEST = 0x1006
    lateinit var dialogLoadingProgress: Dialog
    lateinit var dialogRateDilevery: Dialog
    var isConnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogLoadingProgress = Dialog(this)
        dialogLoadingProgress.setContentView(R.layout.progress_bar_dialog)
        dialogLoadingProgress.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoadingProgress.setCancelable(false)
        LanguageSettings.setLanguage(this, LanguageSettings.getLanguage())
        registerReceiver(
            mNetworkDetectReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        Paper.init(this)
        performDataBinding()
        setViewModel()
        init()

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mNetworkDetectReceiver)

    }


    fun disableUserInteraction() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun enableUserInteraction() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    abstract fun setViewModel()

    abstract fun init()

    private fun performDataBinding() {
        dataBinding = DataBindingUtil.setContentView(this, getLayoutId())
    }


    @LayoutRes
    abstract fun getLayoutId(): Int


    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(
            baseContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermission(permission: String, requestCode: Int) {
        if (!hasPermission(permission)) {
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(permission),
                    requestCode
                )
        }

    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                view.windowToken,
                0
            )
        }
    }

    fun setFragmentWithBack(id: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().addToBackStack(null).replace(id, fragment)
            .commit()
    }

    fun setFragment(id: Int, fragment: Fragment) {
//        supportFragmentManager.beginTransaction().replace(id, fragment).commit()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out,)
            .replace(id, fragment)
            .commit()
    }

    fun setFragmentWithData(id: Int, fragment: Fragment, data: String) {
        val bundle = Bundle()
        bundle.putString("id", data)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(id, fragment).commit()
    }


    fun getViewDataBinding(): D? = dataBinding
    fun restart() {
        finish()
        startActivity(intent)
    }

    private fun checkInternetConnection() {
        val manager: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        if (networkInfo != null && networkInfo.state === NetworkInfo.State.CONNECTED) {
            isConnected = true

//            startActivity(Intent.makeRestartActivityTask(this.intent.component))

            Log.e(TAG, "checkInternetConnection: $isConnected")
//            showNoConnectionSnackBar("Connected", isConnected, 1500)
        } else {
            Log.e(TAG, "checkInternetConnection: $isConnected")
            isConnected = false
//            showNoInternetDialog()
            showNoConnectionSnackBar(
                "No active Internet connection.",
                isConnected,
                5000
            )
        }
    }

    private val mNetworkDetectReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkInternetConnection()
            Log.e(TAG, "onReceive: 1")

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WIFI_ENABLE_REQUEST) {
            Log.e(TAG, "INTERNET: wv wdvadsv dsav adsds adsa a")
        }
    }

    private fun showNoConnectionSnackBar(message: String, isConnected: Boolean, duration: Int) {
        var snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            message, duration
        )
        val sbView: View = snackbar.view
        val textView: TextView = sbView
            .findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        if (!isConnected) {
//            sbView.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))

            sbView.setBackgroundColor(resources.getColor(R.color.red_clair))
            snackbar.setAction(getString(R.string.try_again)) {
                val internetOptionsIntent =
                    Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivityForResult(
                    internetOptionsIntent,
                    WIFI_ENABLE_REQUEST
                )
            }
            snackbar.setActionTextColor(resources.getColor(R.color.white))
        }
        snackbar.show()
    }

    fun showProgressLoadingDialog() {
        Log.e(TAG, "showProgressLoadingDialog: ")
        try {
            val view = LayoutInflater.from(this)
                .inflate(R.layout.progress_bar_dialog, null, false)
            val alertDialogBuilder = AlertDialog.Builder(this, R.style.CustomDialog)
            alertDialogBuilder.setView(view)
            dialogLoadingProgress = alertDialogBuilder.create()
            dialogLoadingProgress.setCanceledOnTouchOutside(false)
            dialogLoadingProgress.setCancelable(false)
            dialogLoadingProgress.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogLoadingProgress.show()
            dialogLoadingProgress.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
//            dialogLoadingProgress.show()
        } catch (e: Exception) {
            Log.e(TAG, "hideProgressLoadingDialog: $e")
        }
    }

    fun hideProgressLoadingDialog() {
        try {
            (dialogLoadingProgress as AlertDialog?)!!.dismiss()
//            dialogLoadingProgress.hide()
        } catch (e: Exception) {
            Log.e(TAG, "hideProgressLoadingDialog: $e")
        }
    }

//    fun showAdsPopup() {
//        try {
//            val inflater = layoutInflater
//            val dialogView: View = inflater.inflate(R.layout.dialog_ads_popup, null)
//            val builder = AlertDialog.Builder(this)
//            builder.setView(dialogView)
//            val dialog = builder.show()
//            val btnSubmit = dialogView.findViewById<TextView>(R.id.tx_show)
//            val btnClose = dialogView.findViewById<ImageView>(R.id.img_close)
//            btnClose.setOnClickListener { v: View? -> dialog.dismiss() }
//            btnSubmit.setOnClickListener { v: View? ->
//                hideKeyboard()
//                dialog.dismiss()
//            }
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        } catch (e: Exception) {
//            Log.e(TAG, "showAdsPopup: $e ")
//        }
//    }

//    fun showToRateStorePopup() {
//        try {
//            val inflater = layoutInflater
//            val dialogView: View = inflater.inflate(R.layout.dialog_to_rate_store_popup, null)
//            val builder = AlertDialog.Builder(this)
//            builder.setView(dialogView)
//            val dialog = builder.show()
//            val btnSubmit = dialogView.findViewById<TextView>(R.id.tx_show)
//            val btnClose = dialogView.findViewById<ImageView>(R.id.img_close)
//            btnClose.setOnClickListener { v: View? -> dialog.dismiss() }
//            btnSubmit.setOnClickListener { v: View? ->
//                hideKeyboard()
//                dialog.dismiss()
//            }
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        } catch (e: Exception) {
//            Log.e(TAG, "showToRateStorePopup: $e ")
//        }
//    }

//    fun showSharePopup() {
//        try {
//            val inflater = layoutInflater
//            val dialogView: View = inflater.inflate(R.layout.dialog_share_popup, null)
//            val builder = AlertDialog.Builder(this)
//            builder.setView(dialogView)
//            val dialog = builder.show()
//            val btnSubmit = dialogView.findViewById<TextView>(R.id.tx_show)
//            val btnClose = dialogView.findViewById<ImageView>(R.id.img_close)
//            btnClose.setOnClickListener { v: View? -> dialog.dismiss() }
//            btnSubmit.setOnClickListener { v: View? ->
//                hideKeyboard()
//                dialog.dismiss()
//            }
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        } catch (e: Exception) {
//            Log.e(TAG, "showSharePopup: $e ")
//        }
//    }

//    fun showGPSPopup() {
//        try {
//            val inflater = layoutInflater
//            val dialogView: View = inflater.inflate(R.layout.dialog_gps_popup, null)
//            val builder = AlertDialog.Builder(this)
//            builder.setView(dialogView)
//            val dialog = builder.show()
//            val btnSubmit = dialogView.findViewById<TextView>(R.id.tx_show)
//            val btnClose = dialogView.findViewById<ImageView>(R.id.img_close)
//            btnClose.setOnClickListener { v: View? -> dialog.dismiss() }
//            btnSubmit.setOnClickListener { v: View? ->
//                hideKeyboard()
//                dialog.dismiss()
//            }
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        } catch (e: Exception) {
//            Log.e(TAG, "showGPSPopup: $e ")
//        }
//    }

//    fun showLogoutPopup() {
//        try {
//            val inflater = layoutInflater
//            val dialogView: View = inflater.inflate(R.layout.dialog_logout_popup, null)
//            val builder = AlertDialog.Builder(this)
//            builder.setView(dialogView)
//            val dialog = builder.show()
//            val btnSubmit = dialogView.findViewById<TextView>(R.id.disconnect_text_view)
//            val btnClose = dialogView.findViewById<TextView>(R.id.cancel_text_view)
//            btnClose.setOnClickListener { v: View? -> dialog.dismiss() }
//            btnSubmit.setOnClickListener { v: View? ->
//                hideKeyboard()
//                Paper.book().delete(Const.USER_INFO)
//                dialog.dismiss()
//                restartApp()
//            }
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        } catch (e: Exception) {
//            Log.e(TAG, "showLogoutPopup: $e ")
//        }
//    }

//    private fun restartApp() {
//        val i = Intent(this, SplashActivity::class.java)
//        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
//    }

     fun updateActionBar(toState: Boolean){
         val actionBar: ActionBar? = supportActionBar
         if(toState)
             actionBar?.show()
         else
             actionBar?.hide()
    }

    fun getLevel(level: Int?): String {
        return if (level == null) {
            "UNKNOWN"
        } else when (level) {
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY -> "LEGACY"
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3 -> "LEVEL_3"
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL -> "EXTERNAL"
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL -> "FULL"
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED -> "LIMITED"
            else -> "UNKNOWN-$level"
        }
    }

    // for socket
    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager: ActivityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

//    fun showToast(to: Int) {
//        when (to) {
//            0 -> Toast.makeText(
//                context,
//                getString(R.string.you_must_choose_valide_number),
//                Toast.LENGTH_SHORT
//            ).show()
//            1 -> Toast.makeText(
//                context,
//                getString(R.string.empty_field),
//                Toast.LENGTH_SHORT
//            ).show()
//            2 -> Toast.makeText(
//                context,
//                getString(R.string.long_name),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
}