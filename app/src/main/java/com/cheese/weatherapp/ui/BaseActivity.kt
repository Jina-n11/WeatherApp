package com.cheese.weatherapp.ui
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB :ViewBinding> :AppCompatActivity() {
    abstract val  LOG_TAG:String

    abstract val bindingInflater : (LayoutInflater) -> VB
    private var _binding : ViewBinding? = null

     val binding  get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =bindingInflater(layoutInflater)
        setContentView(requireNotNull(_binding).root)

        setUp()
        addCallbacks()
    }

    abstract fun setUp()
    abstract fun addCallbacks()
    protected fun log(value:Any){
        Log.v(LOG_TAG,value.toString())
    }

    protected fun showToast(message:String){
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }



}