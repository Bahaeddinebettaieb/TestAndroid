package com.example.testandroid.presentation.ui.mainActivity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testandroid.R
import com.example.testandroid.data.entites.Person
import com.example.testandroid.databinding.ActivityMainBinding
import com.example.testandroid.presentation.base.BaseActivity
import com.example.testandroid.presentation.ui.mainActivity.adapter.PersonAdapter
import java.util.*


class MainActivity : BaseActivity<ActivityMainBinding>(), MainNavigator{
    private val TAG = "MainActivity"
    lateinit var viewModel: MainViewModel
    var constraintValue = false
    var adapterPerson: PersonAdapter? = null
    override fun setViewModel() {
        viewModel = run {
            ViewModelProvider(this)[MainViewModel::class.java]
        }
    }

    override fun init() {
        viewModel.setNavigator(this)
        getViewDataBinding()?.viewModel = viewModel

//        viewModel.personList.observe(this, Observer {
//
//            viewModel.appSharedPref.putObject(AppSharedPreferences.TOKEN_FIREBASE, it?.token)
//        })

        getViewDataBinding()?.constraintInformation?.setOnClickListener(View.OnClickListener {
            collapseView(getViewDataBinding()?.viewConst!!, 400, 10)
            constraintValue = false
            getViewDataBinding()?.imgArrow?.setImageResource(R.drawable.ic_arrow_up)
        })

        getViewDataBinding()?.constraintUp?.setOnClickListener(View.OnClickListener {
            if (constraintValue) {
                collapseView(getViewDataBinding()?.viewConst!!, 400, 10)
                constraintValue = false
                getViewDataBinding()?.imgArrow?.setImageResource(R.drawable.ic_arrow_up)
            } else {
                viewModel.getAllPersons()
                expandView(getViewDataBinding()?.viewConst!!, 400, 700)
                constraintValue = true
                adapterPerson = viewModel.personList.value?.let { it1 -> PersonAdapter(it1) }
                getViewDataBinding()?.recyclerViewPersons?.setHasFixedSize(true)
                getViewDataBinding()?.recyclerViewPersons?.setLayoutManager(LinearLayoutManager(this))
                getViewDataBinding()?.recyclerViewPersons?.setAdapter(adapterPerson)
            }
        })

        getViewDataBinding()?.btnAddPerson?.setOnClickListener {
            val person = Person()
            person.firstname = getViewDataBinding()?.firstnameEt?.text.toString()
            person.lastname = getViewDataBinding()?.lastnameEt?.text.toString()
            person.birthday = getViewDataBinding()?.birthdayEt?.text.toString()
            viewModel.insertPerson(person)
            getViewDataBinding()?.firstnameEt?.setText("")
            getViewDataBinding()?.lastnameEt?.setText("")
            getViewDataBinding()?.birthdayEt?.setText("")
        }


        getViewDataBinding()?.lastnameEt?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                showDatePicker()
                return@OnEditorActionListener true
            }
            false
        })

        getViewDataBinding()?.birthdayLinear?.setOnClickListener {
            showDatePicker()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    @SuppressLint("Range")
    fun expandView(v: View, duration: Int, toHeight: Int) {
        Log.e(TAG, "expandView: $toHeight")
        val value = ValueAnimator.ofInt(v.measuredHeight, toHeight)
        value.addUpdateListener { valueAnimator: ValueAnimator ->
            val x = valueAnimator.animatedValue as Int
            val layoutParams = v.layoutParams
            layoutParams.height = x
            v.layoutParams = layoutParams
        }
        value.duration = duration.toLong()
        value.start()
        getViewDataBinding()?.viewConst?.alpha = 1.5f
    }

    fun collapseView(v: View, duration: Int, targetHeight: Int) {
        Log.e(TAG, "collapseView: " + v.height + targetHeight)
        runOnUiThread {
            val prevHeight = v.height
            val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener { animator ->
                v.layoutParams.height = animator.animatedValue as Int
                v.requestLayout()
            }
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.duration = duration.toLong()
            valueAnimator.start()
            getViewDataBinding()?.viewConst?.alpha = 0.05f
        }
    }

    private fun showDatePicker() {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this,
            { view, year, month, dayOfMonth -> // Handle the selected date
                val selectedDate = (month + 1).toString() + "/" + dayOfMonth + "/" + year
                getViewDataBinding()?.birthdayEt?.setText(selectedDate)
            }, year, month, day
        )
        datePickerDialog.show()
    }

}