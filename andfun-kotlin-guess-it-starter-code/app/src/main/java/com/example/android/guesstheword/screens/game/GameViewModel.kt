package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel  : ViewModel(){

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L
    }


    private val timer: CountDownTimer

    private val _countdown = MutableLiveData<Long>()
    val countdown : LiveData<Long>
        get() = _countdown

    val currentTimeString = Transformations.map(countdown, {time ->
        DateUtils.formatElapsedTime(time)
    })

    // The current word
    private val _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word


    // The current score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score


    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    init {
        _eventGameFinish.value = false
        resetList()
        nextWord()
        _score.value = 0
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _countdown.value = (millisUntilFinished / ONE_SECOND)

            }

            override fun onFinish() {
                // TODO implement what should happen when the timer finishes
                _eventGameFinish.value = true
                _countdown.value = DONE
            }

        }

        timer.start()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
//            _eventGameFinish.value = true
            resetList()
        }
//            gameFinished()
            _word.value = wordList.removeAt(0)
    }

    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    fun onSkip() {
        _score.value = (_score.value)?.minus( 1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus( 1)
        nextWord()
    }

    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }
    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}