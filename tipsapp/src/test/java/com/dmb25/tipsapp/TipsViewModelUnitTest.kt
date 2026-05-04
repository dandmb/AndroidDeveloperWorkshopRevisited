package com.dmb25.tipsapp

import app.cash.turbine.test
import com.dmb25.tipsapp.ui.TipsViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.text.NumberFormat

class TipsViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: TipsViewModel

    @Before
    fun setup() {
        viewModel = TipsViewModel()
    }

    @Test
    fun calculateTip_20PercentNoRoundup() {
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        val actualTip = viewModel.calculateTip(amount = amount, tipPercent, false)
        assertEquals(expectedTip, actualTip)
    }

    @Test
    fun calculateTip_20PercentRoundup() {
        val amount = 158.00
        val tipPercent = 20.00
        val expectedTip = NumberFormat.getCurrencyInstance().format(32)
        val actualTip = viewModel.calculateTip(amount = amount, tipPercent, true)
        assertEquals(expectedTip, actualTip)
    }

    @Test
    fun when_bill_amount_is_empty_tip_is_zero() {
        val amount = 0.0
        val tipPercent = 20.00
        val expectedTip = NumberFormat.getCurrencyInstance().format(0)
        val actualTip = viewModel.calculateTip(amount = amount, tipPercent, true)
        assertEquals(expectedTip, actualTip)
    }

    @Test
    fun when_tip_percentage_is_empty_tip_is_zero() {
        val amount = 10.00
        val tipPercent = 0.0
        val expectedTip = NumberFormat.getCurrencyInstance().format(0)
        val actualTip = viewModel.calculateTip(amount = amount, tipPercent, true)
        assertEquals(expectedTip, actualTip)
    }

    @Test
    fun whenBillAmountAndTipsPercentageAreEmpty_thenTipIsZero() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(initialState.tip, "0.00")
        }
    }

    @Test
    fun `updateTipPercentage with valid percentage updates tipPercentage and tip`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.updateTipPercentage("15")

            val afterPercentageUpdate = awaitItem()
            assertEquals("15", afterPercentageUpdate.tipPercentage)

            val afterTipUpdate = awaitItem()
            assertEquals("15", afterTipUpdate.tipPercentage)
            assertNotNull(afterTipUpdate.tip)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateTipPercentage with invalid string uses zero percentage`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.updateTipPercentage("abc")

            awaitItem()
            val finalState = awaitItem()
            val expectedTip = NumberFormat.getCurrencyInstance().format(0)


            assertEquals("abc", finalState.tipPercentage)
            assertEquals(expectedTip, finalState.tip)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when update round up is called with true, round up is true`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.updateRoundUp(true)

            awaitItem()

            val finalState = awaitItem()

            assertTrue(finalState.roundUp)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when update round up is called with false, round up is false`() = runTest {
        viewModel.uiState.test {

            awaitItem()

            viewModel.updateRoundUp(false)

            val finalState = awaitItem()

            assertEquals(false, finalState.roundUp)

            cancelAndIgnoreRemainingEvents()

        }
    }

    @Test
    fun `when update round up is called with true, tip is updated`()= runTest {

        viewModel.uiState.test {
            awaitItem()
            viewModel.updateRoundUp(true)

            awaitItem()

            val finalState = awaitItem()
            assertEquals(NumberFormat.getCurrencyInstance().format(0), finalState.tip)

        }
    }
}