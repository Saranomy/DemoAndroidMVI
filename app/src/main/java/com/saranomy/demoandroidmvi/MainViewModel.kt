package com.saranomy.demoandroidmvi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {
    // User can only interact to this
    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)

    // State of the views
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState> get() = _state

    // Local data (Model objects)
    private var number = MutableLiveData<Int>().apply { value = 0 }

    init {
        viewModelScope.launch {
            // call actions based on the change of user's intent
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.IncrementIntent -> increment()
                    is MainIntent.ClearIntent -> clear()
                    is MainIntent.SetNumberIntent -> setNumber(it.number)
                }
            }
        }
    }

    private suspend fun increment() {
        // change view state to loading
        _state.value = MainState.Loading

        // do stuff
        delay(250)

        number.value?.let {
            // increment
            number.value = it + 1

            // now assign
            _state.value = try {
                // in case it is null, it should blow up
                MainState.SetNumber(number.value!!)
            } catch (e: Exception) {
                // assign error view instead
                MainState.Error(e.localizedMessage)
            }
        }

        // finish doing stuff, back to idle
        delay(250)
        _state.value = MainState.Idle
    }

    private suspend fun clear() {
        _state.value = MainState.Loading
        delay(250)
        number.value?.let {
            number.value = 0
            _state.value = try {
                MainState.SetNumber(number.value!!)
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
        delay(250)
        _state.value = MainState.Idle
    }

    private suspend fun setNumber(value: Int) {
        _state.value = MainState.Loading
        delay(250)
        number.value?.let {
            number.value = value
            _state.value = try {
                MainState.SetNumber(number.value!!)
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
        delay(250)
        _state.value = MainState.Idle
    }
}