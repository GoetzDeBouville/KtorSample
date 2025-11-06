package com.sample.ktorsample.feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.ktorsample.core.domain.models.ErrorType
import com.sample.ktorsample.core.domain.models.Result
import com.sample.ktorsample.core.domain.models.coins.CoinModel
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel
import com.sample.ktorsample.feature.domain.api.BinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinanceViewModel @Inject constructor(
    private val repo: BinanceRepository
) : ViewModel() {
    data class State(
        val isLoadingPrices: Boolean = false,
        val isLoadingTicker: Boolean = false,
        val prices: List<CoinShortModel> = emptyList(),
        val selectedSymbol: String? = null,
        val ticker: CoinModel? = null,
        val error: ErrorType? = null,
        val isTickerDialogVisible: Boolean = false,
    )

    sealed interface Intent {
        data object LoadAllPrices : Intent
        data class SelectSymbol(val symbol: String) : Intent
        data object RefreshTicker : Intent
        data object ClearError : Intent
        data object DismissTickerDialog : Intent
    }

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadAllPrices()
        }
    }

    fun accept(intent: Intent) {
        when (intent) {
            is Intent.LoadAllPrices -> loadAllPrices()
            is Intent.SelectSymbol -> selectSymbol(intent.symbol)
            is Intent.RefreshTicker -> refreshTicker()
            is Intent.ClearError -> _state.update { it.copy(error = null) }
            is Intent.DismissTickerDialog -> _state.update { it.copy(isTickerDialogVisible = false) }
        }
    }

    private fun loadAllPrices() {
        _state.update { it.copy(isLoadingPrices = true, error = null) }
        viewModelScope.launch {
            repo.getAllPrices()
                .onCompletion { _state.update { s -> s.copy(isLoadingPrices = false) } }
                .collect { result ->
                    when (result) {
                        is Result.Success -> _state.update { it.copy(prices = result.data) }
                        is Result.Error   -> _state.update { it.copy(error = result.error) }
                    }
                }
        }
    }

    private fun selectSymbol(symbol: String) {
        _state.update {
            it.copy(
                selectedSymbol = symbol,
                ticker = null,
                error = null,
                isTickerDialogVisible = true
            )
        }
        refreshTicker()
    }

    private fun refreshTicker() {
        val symbol = _state.value.selectedSymbol ?: return
        _state.update { it.copy(isLoadingTicker = true, error = null) }
        viewModelScope.launch {
            repo.get24hTicker(symbol)
                .onCompletion { _state.update { s -> s.copy(isLoadingTicker = false) } }
                .collect { result ->
                    when (result) {
                        is Result.Success -> _state.update { it.copy(ticker = result.data) }
                        is Result.Error -> _state.update { it.copy(error = result.error) }
                    }
                }
        }
    }
}
