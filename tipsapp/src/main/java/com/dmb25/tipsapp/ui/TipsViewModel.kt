package com.dmb25.tipsapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat


data class UiState(
    val tip: String = "0.00",
    val tipPercentage: String = "",
    val roundUp: Boolean = false,
    val billAmount: String = "",
)

class TipsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()


    fun updateTipPercentage(tipPercentage: String) {
        _uiState.update { currentState ->
            currentState.copy(tipPercentage = tipPercentage)
        }
        val percentageDouble = tipPercentage.toDoubleOrNull() ?: 0.0
        val tip = calculateTip(
            tipPercentage = percentageDouble,
            amount = _uiState.value.billAmount.toDoubleOrNull() ?: 0.0,
            roundUp = _uiState.value.roundUp
        )
        _uiState.update { currentState ->
            currentState.copy(tip = tip)
        }
    }


    fun updateRoundUp(roundUp: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(roundUp = roundUp)
        }
        val tip = calculateTip(
            roundUp = roundUp,
            tipPercentage = _uiState.value.tipPercentage.toDoubleOrNull() ?: 0.0,
            amount = _uiState.value.billAmount.toDoubleOrNull() ?: 0.0
        )
        _uiState.update { currentState ->
            currentState.copy(tip = tip)
        }
    }


    fun updateBillAmount(billAmount: String) {
        _uiState.value = _uiState.value.copy(billAmount = billAmount)
        val amountDouble = billAmount.toDoubleOrNull() ?: 0.0
        val tip = calculateTip(
            amountDouble,
            roundUp = _uiState.value.roundUp,
            tipPercentage = _uiState.value.tipPercentage.toDoubleOrNull() ?: 0.0
        )
        _uiState.update { currentState ->
            currentState.copy(tip = tip)
        }
    }


    fun calculateTip(
        amount: Double = 0.0,
        tipPercentage: Double = 15.0,
        roundUp: Boolean = false
    ): String {
        var tip = tipPercentage / 100 * amount
        if (roundUp) {
            tip = kotlin.math.ceil(tip)
        }
        return NumberFormat.getCurrencyInstance().format(tip)
    }


}