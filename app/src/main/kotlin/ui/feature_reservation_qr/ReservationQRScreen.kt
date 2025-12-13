package com.application.ui.feature_reservation_qr

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.navigation.RootRoute
import com.application.ui.components.PrimaryButton
import com.application.ui.feature_order_confirmation.QRCodeGenerator
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorRed
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationQRScreen(
    viewModel: ReservationQRViewModel,
    navController: NavController,
    reservationId: String
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    // Generate QR code bitmap
    val qrBitmap = remember(state.qrCodeData) {
        if (state.qrCodeData.isNotEmpty()) {
            QRCodeGenerator.generateQRCode(state.qrCodeData, 400, 400)
        } else {
            null
        }
    }

    LaunchedEffect(reservationId) {
        viewModel.setEvent(ReservationQREvent.OnScreenShown(reservationId))
    }

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is ReservationQREffect.NavigateToHome -> {
                    navController.navigate(RootRoute.Home.route) {
                        popUpTo(RootRoute.Home.route) { inclusive = false }
                    }
                }
                is ReservationQREffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Back",
                    color = colorRed,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickableNoRipple { viewModel.setEvent(ReservationQREvent.OnBackClicked) }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Scrollable content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Title: Reservation Complete
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = colorBluePrimary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Reservation Complete",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorWhitePure,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Subtitle
                item {
                    Text(
                        text = "Show this at the entrance",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorWhitePure,
                        textAlign = TextAlign.Center
                    )
                }

                // QR Code
                item {
                    if (qrBitmap != null) {
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .background(
                                    color = colorWhitePure,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    } else if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(280.dp),
                            color = colorWhitePure
                        )
                    }
                }

                // Reservation Number
                item {
                    if (state.reservationId.isNotEmpty()) {
                        Text(
                            text = "Reservation #${state.reservationId}",
                            style = MaterialTheme.typography.titleLarge,
                            color = colorWhitePure,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Reservation Details
                item {
                    state.reservation?.let { reservation ->
                        ReservationDetailsCard(reservation = reservation)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Fixed button at bottom
            PrimaryButton(
                onClick = { viewModel.setEvent(ReservationQREvent.OnBackToHomeClicked) },
                text = "Back to Home",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
fun ReservationDetailsCard(reservation: ReservationDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorBackgroundMain.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name and Date/Time row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = reservation.name,
                style = MaterialTheme.typography.titleLarge,
                color = colorWhitePure
            )
            Text(
                text = reservation.formattedDateTime,
                style = MaterialTheme.typography.bodyMedium,
                color = colorWhitePure
            )
        }
        
        // Zone and Seat row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Zone: ${reservation.zone}",
                style = MaterialTheme.typography.bodyMedium,
                color = colorWhitePure
            )
            Text(
                text = "Seat: ${reservation.seatNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = colorWhitePure
            )
        }
    }
}


