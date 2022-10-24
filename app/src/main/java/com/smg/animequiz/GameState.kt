package com.smg.animequiz

enum class State{
    LOADING,
    WAITING_INPUT
}

class GameState {
    var state: State = State.WAITING_INPUT
    var correctCount: Int = 0
}