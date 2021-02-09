package com.saranomy.demoandroidmvi

// Different states the View can be shown
sealed class MainState {
    object Idle : MainState()
    object Loading : MainState()
    data class SetNumber(val number: Int) : MainState()
    data class Error(val error: String?) : MainState()
}