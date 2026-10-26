package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AgroRepository
import com.example.model.CartItem
import com.example.model.FarmOrder
import com.example.model.TreatmentProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DoseCalculation(
    val product: TreatmentProduct,
    val farmPlotAcres: Double = 1.0,
    val totalRequiredUnits: Double = 0.0,
    val recommendedPacksCount: Int = 1,
    val totalWaterDilutionLiters: Double = 0.0,
    val estimatedTotalCostUsd: Double = 0.0
)

data class StoreUiState(
    val selectedCategory: String = "All",
    val searchQuery: String = "",
    val activeDosageCalculation: DoseCalculation? = null,
    val isCartOpen: Boolean = false,
    val isCheckoutOpen: Boolean = false,
    val farmAddressInput: String = "Plot #4, Green Valley Road, Agro-Zone 2",
    val orderSuccessNotification: FarmOrder? = null
)

class StoreViewModel(private val repository: AgroRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    val products: StateFlow<List<TreatmentProduct>> = repository.catalog

    val cartItems: StateFlow<List<CartItem>> = repository.cartFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<FarmOrder>> = repository.ordersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun openDosageCalculator(product: TreatmentProduct, initialAcres: Double = 2.0) {
        val totalUnits = product.recommendedDosagePerAcre * initialAcres
        val packs = kotlin.math.ceil(totalUnits / product.packUnitsPerBottle).toInt().coerceAtLeast(1)
        val water = product.dilutionWaterLitersPerAcre * initialAcres
        val cost = packs * product.priceUsd

        _uiState.value = _uiState.value.copy(
            activeDosageCalculation = DoseCalculation(
                product = product,
                farmPlotAcres = initialAcres,
                totalRequiredUnits = totalUnits,
                recommendedPacksCount = packs,
                totalWaterDilutionLiters = water,
                estimatedTotalCostUsd = cost
            )
        )
    }

    fun recalculateDosage(acres: Double) {
        val current = _uiState.value.activeDosageCalculation ?: return
        val clampedAcres = acres.coerceIn(0.1, 500.0)
        val totalUnits = current.product.recommendedDosagePerAcre * clampedAcres
        val packs = kotlin.math.ceil(totalUnits / current.product.packUnitsPerBottle).toInt().coerceAtLeast(1)
        val water = current.product.dilutionWaterLitersPerAcre * clampedAcres
        val cost = packs * current.product.priceUsd

        _uiState.value = _uiState.value.copy(
            activeDosageCalculation = current.copy(
                farmPlotAcres = clampedAcres,
                totalRequiredUnits = totalUnits,
                recommendedPacksCount = packs,
                totalWaterDilutionLiters = water,
                estimatedTotalCostUsd = cost
            )
        )
    }

    fun closeDosageCalculator() {
        _uiState.value = _uiState.value.copy(activeDosageCalculation = null)
    }

    fun addCalculatedDoseToCart() {
        val current = _uiState.value.activeDosageCalculation ?: return
        viewModelScope.launch {
            repository.addToCart(
                product = current.product,
                quantity = current.recommendedPacksCount,
                acres = current.farmPlotAcres
            )
            closeDosageCalculator()
            _uiState.value = _uiState.value.copy(isCartOpen = true)
        }
    }

    fun quickAddToCart(product: TreatmentProduct) {
        viewModelScope.launch {
            repository.addToCart(product = product, quantity = 1, acres = 1.0)
        }
    }

    fun updateCartItemQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(productId, quantity)
        }
    }

    fun removeCartItem(productId: String) {
        viewModelScope.launch {
            repository.removeCartItem(productId)
        }
    }

    fun toggleCart(open: Boolean) {
        _uiState.value = _uiState.value.copy(isCartOpen = open)
    }

    fun openCheckout() {
        _uiState.value = _uiState.value.copy(isCartOpen = false, isCheckoutOpen = true)
    }

    fun closeCheckout() {
        _uiState.value = _uiState.value.copy(isCheckoutOpen = false)
    }

    fun updateFarmAddress(address: String) {
        _uiState.value = _uiState.value.copy(farmAddressInput = address)
    }

    fun confirmOrder() {
        viewModelScope.launch {
            val order = repository.placeOrder(_uiState.value.farmAddressInput)
            _uiState.value = _uiState.value.copy(
                isCheckoutOpen = false,
                orderSuccessNotification = order
            )
        }
    }

    fun dismissOrderSuccess() {
        _uiState.value = _uiState.value.copy(orderSuccessNotification = null)
    }
}
