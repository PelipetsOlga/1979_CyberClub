package com.application.ui.feature_home_wrapper.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.domain.model.ReservationHistoryItem
import com.application.domain.model.OrderHistoryItem
import com.application.domain.model.ReservationStatus
import com.application.domain.model.OrderStatus
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBackgroundSurface
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorGreen
import com.application.ui.theme.colorRed
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    navController: NavController,
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(Unit) {
        viewModel.setEvent(HistoryEvent.OnScreenShown)
    }

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is HistoryEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            TopAppBar(
                colors = topAppBarColors,
                title = {
                    Text(
                        text = "History",
                        color = colorWhitePure,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    Text(
                        text = "Back",
                        color = colorRed,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickableNoRipple { viewModel.setEvent(HistoryEvent.OnBackClicked) }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TabButton(
                    text = "Reservations",
                    isSelected = state.selectedTab == HistoryTab.RESERVATIONS,
                    onClick = { viewModel.setEvent(HistoryEvent.OnTabSelected(HistoryTab.RESERVATIONS)) },
                    modifier = Modifier.weight(1f)
                )
                TabButton(
                    text = "Orders",
                    isSelected = state.selectedTab == HistoryTab.ORDERS,
                    onClick = { viewModel.setEvent(HistoryEvent.OnTabSelected(HistoryTab.ORDERS)) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Content
            when (state.selectedTab) {
                HistoryTab.RESERVATIONS -> {
                    if (state.reservations.isEmpty()) {
                        EmptyState(
                            message = "No past reservations",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.reservations) { reservation ->
                                ReservationHistoryCard(reservation = reservation)
                            }
                        }
                    }
                }
                HistoryTab.ORDERS -> {
                    if (state.orders.isEmpty()) {
                        EmptyState(
                            message = "No past orders",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.orders) { order ->
                                OrderHistoryCard(order = order)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) colorBluePrimary else colorWhitePure.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = colorBackgroundMain,
                shape = RoundedCornerShape(12.dp)
            )
            .clickableNoRipple { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = colorWhitePure,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ReservationHistoryCard(reservation: ReservationHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorBackgroundSurface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Reservation ID and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reservation #${reservation.reservationId}",
                    color = colorBluePrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StatusBadge(
                    text = if (reservation.status == ReservationStatus.COMPLETED) "Completed" else "Cancelled",
                    isCompleted = reservation.status == ReservationStatus.COMPLETED
                )
            }

            // Name and Date/Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reservation.name,
                    color = colorWhitePure,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${reservation.date}, ${reservation.time}",
                    color = colorWhitePure,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Zone and Seat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Zone: ${reservation.zone}",
                    color = colorWhitePure,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Seat: ${reservation.seatNumber}",
                    color = colorWhitePure,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun OrderHistoryCard(order: OrderHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorBackgroundSurface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Order ID and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.orderId}",
                    color = colorBluePrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StatusBadge(
                    text = if (order.status == OrderStatus.COMPLETED) "Completed" else "Cancelled",
                    isCompleted = order.status == OrderStatus.COMPLETED
                )
            }

            // Items and Date/Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    order.items.forEach { item ->
                        Text(
                            text = "- ${item.title}${if (item.quantity > 1) " × ${item.quantity}" else ""}",
                            color = colorWhitePure,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Text(
                    text = "${order.date}, ${order.time}",
                    color = colorWhitePure,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Total
            Text(
                text = "Total: $${String.format("%.0f", order.total)}",
                color = colorWhitePure,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StatusBadge(text: String, isCompleted: Boolean) {
    Box(
        modifier = Modifier
            .background(
                color = if (isCompleted) colorGreen else colorRed,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = colorWhitePure,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmptyState(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = colorWhitePure.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

