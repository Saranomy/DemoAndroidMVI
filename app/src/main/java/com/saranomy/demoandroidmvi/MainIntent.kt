package com.saranomy.demoandroidmvi

// All Intentions user can possibly do
sealed class MainIntent {
    object IncrementIntent : MainIntent()
    object ClearIntent : MainIntent()
    data class SetNumberIntent(val number: Int) : MainIntent()
}