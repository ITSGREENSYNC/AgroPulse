package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderStatus
import com.example.model.TreatmentProduct
import com.example.ui.theme.ForestGreen40
import com.example.ui.theme.HarvestGold40
import com.example.ui.viewmodels.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    storeViewModel: StoreViewModel,
    currentLanguage: com.example.model.AppLanguage = com.example.model.AppLanguage.EN,
    onOpenLanguageModal: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val loc = com.example.data.LocStrings.get(currentLanguage)
    val uiState by storeViewModel.uiState.collectAsState()
    val products by storeViewModel.products.collectAsState()
    val cartItems by storeViewModel.cartItems.collectAsState()
    val orders by storeViewModel.orders.collectAsState()

    var activeTab by remember { mutableIntStateOf(0) } // 0: Catalog, 1: Orders
    val categories = listOf("All", "Bio-Pesticide", "Fungicide", "Biological", "Nutrient", "Organic")

    val filteredProducts = products.filter { prod ->
        val matchesCategory = when (uiState.selectedCategory) {
            "All" -> true
            "Bio-Pesticide" -> prod.category.contains("Bio-Insecticide", ignoreCase = true) || prod.category.contains("Pesticide", ignoreCase = true)
            "Fungicide" -> prod.category.contains("Fungicide", ignoreCase = true)
            "Biological" -> prod.category.contains("Biological", ignoreCase = true)
            "Nutrient" -> prod.category.contains("Nutrient", ignoreCase = true) || prod.category.contains("Stimulant", ignoreCase = true)
            "Organic" -> prod.organicCertified
            else -> true
        }
        val matchesSearch = prod.name.contains(uiState.searchQuery, ignoreCase = true) ||
                prod.activeIngredient.contains(uiState.searchQuery, ignoreCase = true) ||
                prod.targetDiseases.any { it.contains(uiState.searchQuery, ignoreCase = true) }
        matchesCategory && matchesSearch
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("marketplace_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar with Cart Icon & Language Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = loc.storeTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = loc.storeSubtitle,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = ForestGreen40.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .clickable { onOpenLanguageModal() }
                            .padding(end = 4.dp)
                            .testTag("store_language_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = ForestGreen40,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentLanguage.nativeName,
                                color = ForestGreen40,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = { storeViewModel.toggleCart(true) },
                        modifier = Modifier.testTag("open_cart_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge(containerColor = HarvestGold40) {
                                        Text(
                                            text = cartItems.sumOf { it.quantity }.toString(),
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = "Cart",
                                tint = ForestGreen40,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Tab Row (Catalog vs My Farm Orders)
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = ForestGreen40
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = { Text(loc.catalogTab, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("catalog_tab")
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(loc.ordersTab, fontWeight = FontWeight.Bold)
                            if (orders.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = ForestGreen40,
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = "${orders.size}",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.testTag("orders_tab")
                )
            }

            if (activeTab == 0) {
                // Search Input
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { storeViewModel.updateSearchQuery(it) },
                    placeholder = { Text(loc.searchPlaceholder) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ForestGreen40) },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { storeViewModel.updateSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForestGreen40,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("search_treatments_input")
                )

                // Category Filter Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = uiState.selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { storeViewModel.selectCategory(cat) },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ForestGreen40,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("category_chip_$cat")
                        )
                    }
                }

                // Treatment Catalog List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onCalculateDose = { storeViewModel.openDosageCalculator(product) },
                            onQuickAdd = { storeViewModel.quickAddToCart(product) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            } else {
                // Farm Orders List
                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Farm Deliveries Yet",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Calculated treatments you order will appear here with live tracking to your farm depot.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(orders) { order ->
                            OrderCard(order = order)
                        }
                        item {
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }
                }
            }
        }
    }

    // Interactive Dosage Calculator Bottom Sheet
    uiState.activeDosageCalculation?.let { calc ->
        ModalBottomSheet(
            onDismissRequest = { storeViewModel.closeDosageCalculator() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            modifier = Modifier.testTag("dosage_calculator_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Acreage Dosage Calculator",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = calc.product.name,
                    fontSize = 14.sp,
                    color = ForestGreen40,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Acreage Input & Slider
                Text(
                    text = "Farm Plot Size: ${String.format("%.1f", calc.farmPlotAcres)} Acres",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Slider(
                    value = calc.farmPlotAcres.toFloat(),
                    onValueChange = { storeViewModel.recalculateDosage(it.toDouble()) },
                    valueRange = 0.5f..20f,
                    steps = 39,
                    colors = SliderDefaults.colors(
                        thumbColor = ForestGreen40,
                        activeTrackColor = ForestGreen40
                    ),
                    modifier = Modifier.testTag("acres_slider")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1.0, 2.5, 5.0, 10.0).forEach { preset ->
                        OutlinedButton(
                            onClick = { storeViewModel.recalculateDosage(preset) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("${preset} ac", fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Calculated Results Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Active Chemical / Bio:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "${calc.totalRequiredUnits.toInt()} ${calc.product.dosageUnit.split("/")[0]}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Water Dilution Needed:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "${calc.totalWaterDilutionLiters.toInt()} Liters",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Recommended Packs to Buy:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "${calc.recommendedPacksCount} x ${calc.product.packSize}",
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen40,
                                fontSize = 13.sp
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Estimated Total Cost:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                "$${String.format("%.2f", calc.estimatedTotalCostUsd)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = ForestGreen40
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { storeViewModel.addCalculatedDoseToCart() },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("add_calculated_dose_button")
                ) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Add ${calc.recommendedPacksCount} Pack(s) to Cart • $${String.format("%.2f", calc.estimatedTotalCostUsd)}",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Cart Modal Sheet
    if (uiState.isCartOpen) {
        ModalBottomSheet(
            onDismissRequest = { storeViewModel.toggleCart(false) },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            modifier = Modifier.testTag("cart_bottom_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Farm Supply Cart (${cartItems.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { storeViewModel.toggleCart(false) }) {
                        Icon(Icons.Default.Close, contentDescription = "Close Cart")
                    }
                }

                if (cartItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Your farm cart is empty.", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(cartItems) { item ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(item.product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("${item.product.packSize} • Configured for ${item.targetPlotAcres} ac", fontSize = 12.sp, color = Color.Gray)
                                        Text("$${String.format("%.2f", item.product.priceUsd * item.quantity)}", fontWeight = FontWeight.Bold, color = ForestGreen40)
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = { storeViewModel.updateCartItemQuantity(item.product.id, item.quantity - 1) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Remove,
                                                contentDescription = "Decrease",
                                                tint = if (item.quantity == 1) Color.Red else Color.Black
                                            )
                                        }
                                        Text("${item.quantity}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                                        IconButton(
                                            onClick = { storeViewModel.updateCartItemQuantity(item.product.id, item.quantity + 1) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "Increase")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    val subtotal = cartItems.sumOf { it.product.priceUsd * it.quantity }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal:", fontSize = 14.sp)
                        Text("$${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Agro-Depot Express Delivery:", fontSize = 14.sp)
                        Text("FREE (Farm Promo)", fontWeight = FontWeight.Bold, color = ForestGreen40, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { storeViewModel.openCheckout() },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("proceed_checkout_button")
                    ) {
                        Text("Proceed to Checkout • $${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Checkout Sheet
    if (uiState.isCheckoutOpen) {
        ModalBottomSheet(
            onDismissRequest = { storeViewModel.closeCheckout() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            modifier = Modifier.testTag("checkout_bottom_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Confirm Farm Delivery",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.farmAddressInput,
                    onValueChange = { storeViewModel.updateFarmAddress(it) },
                    label = { Text("Farm / Depot Delivery Address") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen40),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("farm_address_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Payment Method", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Agri-Credit / Instant Farm Wire (Approved)", fontSize = 13.sp, color = ForestGreen40)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Direct dispatch from regional agricultural warehouse within 24 hours.", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { storeViewModel.confirmOrder() },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_farm_order_button")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Place Order for Dispatch", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ProductCard(
    product: TreatmentProduct,
    onCalculateDose: () -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.category.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen40,
                        letterSpacing = 0.5.sp
                    )
                    if (product.organicCertified) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "ORGANIC",
                                color = Color(0xFF2E7D32),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = HarvestGold40, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "${product.rating} (${product.reviewsCount})", fontSize = 11.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Active: ${product.activeIngredient}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = product.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Pack: ${product.packSize}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Rate: ${product.recommendedDosagePerAcre.toInt()} ${product.dosageUnit}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = ForestGreen40
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${String.format("%.2f", product.priceUsd)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onCalculateDose,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen40),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen40),
                        modifier = Modifier.testTag("calc_dose_${product.id}")
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Dose Calc", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onQuickAdd,
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("quick_add_${product.id}")
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: com.example.model.FarmOrder) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.orderId,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = ForestGreen40
                )
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = order.status.label,
                        color = Color(0xFF2E7D32),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Estimated Arrival: ${order.estimatedArrivalDate}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Destination: ${order.farmAddress}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { 0.65f },
                color = ForestGreen40,
                trackColor = Color(0xFFE0E0E0),
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Paid:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$${String.format("%.2f", order.totalUsd)}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
