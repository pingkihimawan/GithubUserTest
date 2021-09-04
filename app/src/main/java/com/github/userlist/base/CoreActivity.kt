package com.github.userlist.base

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import java.lang.reflect.ParameterizedType
import javax.inject.Inject


abstract class CoreActivity<B : ViewDataBinding, VM : ViewModel> : DaggerAppCompatActivity(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelClass = (javaClass
            .genericSuperclass as ParameterizedType)
            .actualTypeArguments[1] as Class<VM>
        viewModel = ViewModelProvider(this, viewModelFactory).get(viewModelClass)

    }

    protected fun setLayoutRes(layoutResID: Int) {
        binding = DataBindingUtil.setContentView(this, layoutResID)
    }

    fun showToast(string: String){
        Toast.makeText(this@CoreActivity, string, Toast.LENGTH_LONG).show()
    }

}