package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord


    private var wordList: MutableList<String> = mutableListOf()
    lateinit var currentWord: String

    init {
        getNextWord()
    }

    private fun getNextWord() {
        currentWord = allWordsList.random().toUpperCase()
        val tempWord = currentWord.toUpperCase().toCharArray()
        tempWord.shuffle()
        while (tempWord.toString() == currentWord) {
            tempWord.shuffle()
        }
        if (wordList != null) {
            if (wordList.contains(currentWord)) {
                getNextWord()
            } else {
                wordList.add(currentWord)
                _currentScrambledWord.value = String(tempWord)
                _currentWordCount.value = _currentWordCount.value?.inc()
            }
        }
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else {
            false
        }
    }

    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        Log.d("toast", playerWord)
        Log.d("toast", currentWord)
        if (playerWord == currentWord) {
            increaseScore()
            return true
        }
        return false
    }

    fun restartGame() {
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWord()
    }
}