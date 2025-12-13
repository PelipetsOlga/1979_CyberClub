package com.application.ui.feature_home_wrapper.reserve_seat.ui.feature_home_wrapper.reserve_seat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.domain.model.Zone
import com.application.navigation.RootRoute
import com.application.ui.components.MenuButton
import com.application.ui.components.PrimaryButton
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReserveSeatScreen(
    viewModel: ReserveSeatViewModel,
    navController: NavController,
    rootNavController: NavController,
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is ReserveSeatEffect.NavigateToReservationQR -> {
                    rootNavController.navigate(RootRoute.ReservationQR.createRoute(it.reservationId)) {
                        launchSingleTop = true
                    }
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
                        text = "Reserve Your Seat",
                        color = colorWhitePure
                    )
                },
                navigationIcon = {
                    MenuButton(onMenuClick)
                }
            )
        }
    ) { paddingValues ->
        ReserveSeatScreenContent(
            state = state,
            onEvent = { viewModel.setEvent(it) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
fun ReserveSeatScreenContent(
    state: ReserveSeatState,
    onEvent: (ReserveSeatEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Name field
            ReservationTextField(
                label = "Name",
                value = state.name,
                placeholder = "Enter your name",
                onValueChange = { onEvent(ReserveSeatEvent.OnNameChanged(it)) }
            )

            // Phone field
            ReservationTextField(
                label = "Phone",
                value = state.phone,
                placeholder = "Enter your phone number",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                onValueChange = { onEvent(ReserveSeatEvent.OnPhoneChanged(it)) }
            )

            // Zone dropdown
            ReservationDropdownField(
                label = "Zone",
                selectedValue = state.selectedZone?.name ?: "",
                placeholder = "Select zone",
                options = Zone.values().map { it.name },
                onValueSelected = { zoneName ->
                    onEvent(ReserveSeatEvent.OnZoneSelected(Zone.valueOf(zoneName)))
                }
            )

            // Seat Number dropdown
            ReservationDropdownField(
                label = "Seat Number",
                selectedValue = state.selectedSeatNumber,
                placeholder = "Select seat",
                options = state.availableSeats,
                enabled = state.selectedZone != null,
                onValueSelected = { seat ->
                    onEvent(ReserveSeatEvent.OnSeatNumberSelected(seat))
                }
            )

            // Date selection
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorWhitePure
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    state.availableDates.forEach { date ->
                        DateButton(
                            date = date,
                            isSelected = state.selectedDate == date,
                            onClick = { onEvent(ReserveSeatEvent.OnDateSelected(date)) }
                        )
                    }
                }
            }

            // Time selection
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorWhitePure
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    state.availableTimes.forEach { time ->
                        TimeButton(
                            time = time,
                            isSelected = state.selectedTime == time,
                            onClick = { onEvent(ReserveSeatEvent.OnTimeSelected(time)) }
                        )
                    }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    color = colorWhitePure
                )
            }

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // Confirm Reservation button - fixed at bottom
        PrimaryButton(
            onClick = { onEvent(ReserveSeatEvent.OnConfirmReservation) },
            text = "Confirm Reservation",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )
    }
}

@Composable
fun ReservationTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = colorWhitePure,
                modifier = Modifier.width(100.dp)
            )
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = colorWhitePure.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorWhitePure,
                    unfocusedTextColor = colorWhitePure,
                    focusedBorderColor = colorBluePrimary.copy(alpha = 0.3f),
                    unfocusedBorderColor = colorBluePrimary.copy(alpha = 0.3f),
                    focusedContainerColor = colorBackgroundMain,
                    unfocusedContainerColor = colorBackgroundMain
                ),
                singleLine = true,
                keyboardOptions = keyboardOptions
            )
        }
        HorizontalDivider(
            color = colorWhitePure.copy(alpha = 0.3f),
            thickness = 1.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDropdownField(
    label: String,
    selectedValue: String,
    placeholder: String,
    options: List<String>,
    enabled: Boolean = true,
    onValueSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = colorWhitePure,
                modifier = Modifier.width(100.dp)
            )
            Box(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (enabled) colorBackgroundMain else colorBackgroundMain.copy(alpha = 0.5f)
                        )
                        .border(
                            width = 1.dp,
                            color = colorBluePrimary.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickableNoRipple(enabled = enabled) { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedValue.ifEmpty { placeholder },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedValue.isEmpty()) colorWhitePure.copy(alpha = 0.5f) else colorWhitePure
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = colorWhitePure.copy(alpha = 0.7f)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorBackgroundMain)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    color = colorWhitePure
                                )
                            },
                            onClick = {
                                onValueSelected(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        HorizontalDivider(
            color = colorWhitePure.copy(alpha = 0.3f),
            thickness = 1.dp
        )
    }
}

@Composable
fun DateButton(
    date: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val parts = date.split(" ")
    val dayName = parts[0]
    val dayNumber = parts.getOrElse(1) { "" }

    Box(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (isSelected) colorBluePrimary else colorBackgroundMain,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = colorBluePrimary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickableNoRipple { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) colorWhitePure else colorWhitePure.copy(alpha = 0.7f)
            )
            Text(
                text = dayNumber,
                style = MaterialTheme.typography.titleLarge,
                color = colorWhitePure
            )
        }
    }
}

@Composable
fun TimeButton(
    time: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (isSelected) colorBluePrimary else colorBackgroundMain,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = colorBluePrimary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickableNoRipple { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.titleMedium,
            color = colorWhitePure
        )
    }
}
