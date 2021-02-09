package com.saranomy.demoandroidmvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var textViewNumber: TextView
    private lateinit var buttonIncrement: Button
    private lateinit var buttonRandomize: Button
    private lateinit var buttonClear: Button

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewNumber = findViewById(R.id.number)
        buttonIncrement = findViewById(R.id.increment)
        buttonRandomize = findViewById(R.id.randomize)
        buttonClear = findViewById(R.id.clear)

        lifecycleScope.launch {
            // handle different states of View
            mainViewModel.state.collect {
                when (it) {
                    is MainState.Idle -> {
                        buttonIncrement.isEnabled = true
                        buttonRandomize.isEnabled = true
                        buttonClear.isEnabled = true
                    }
                    is MainState.Loading -> {
                        textViewNumber.text = ""
                        buttonIncrement.isEnabled = false
                        buttonRandomize.isEnabled = false
                        buttonClear.isEnabled = false
                    }
                    is MainState.SetNumber -> {
                        textViewNumber.text = it.number.toString()
                        buttonIncrement.isEnabled = false
                        buttonRandomize.isEnabled = false
                        buttonClear.isEnabled = false
                    }
                    is MainState.Error -> {
                        textViewNumber.text = it.error
                        buttonIncrement.isEnabled = false
                        buttonRandomize.isEnabled = false
                        buttonClear.isEnabled = false
                    }
                }
            }
        }
    }

    fun onClickIncrement(view: View) {
        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.IncrementIntent)
        }
    }

    fun onClickRandomize(view: View) {
        lifecycleScope.launch {
            val number = (Math.random() * 10).toInt()
            mainViewModel.userIntent.send(MainIntent.SetNumberIntent(number))
        }
    }

    fun onClickClear(view: View) {
        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.ClearIntent)
        }
    }
}