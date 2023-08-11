package com.example.testandroid.presentation.ui.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.testandroid.R
import com.example.testandroid.databinding.ActivityMainBinding
import com.example.testandroid.presentation.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(), MainNavigator{
    private val TAG = "MainActivity"
    lateinit var viewModel: MainViewModel


    override fun setViewModel() {
        viewModel = run {
            ViewModelProvider(this)[MainViewModel::class.java]
        }
    }

    override fun init() {
        viewModel.setNavigator(this)
        getViewDataBinding()!!.viewModel = viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

}