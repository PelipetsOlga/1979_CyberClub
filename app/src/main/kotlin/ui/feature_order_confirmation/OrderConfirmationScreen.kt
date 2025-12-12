package com.application.ui.feature_order_confirmation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorRed
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple
import android.graphics.Bitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationScreen(
    viewModel: OrderConfirmationViewModel,
    navController: NavController
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

    LaunchedEffect(Unit) {
        viewModel.setEvent(OrderConfirmationEvent.OnScreenShown)
    }

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is OrderConfirmationEffect.NavigateToHome -> {
                    navController.navigate(RootRoute.Home.route) {
                        popUpTo(RootRoute.Home.route) { inclusive = false }
                    }
                }
                is OrderConfirmationEffect.NavigateBack -> {
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
                        .align(Alignment.CenterStart)
                        .clickableNoRipple { viewModel.setEvent(OrderConfirmationEvent.OnBackClicked) }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Title: Order Confirmed!
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
                        text = "Order Confirmed!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = colorWhitePure,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Subtitle
            item {
                Text(
                    text = "Show this QR code at the counter",
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
            
            // Order Number
            item {
                if (state.orderId.isNotEmpty()) {
                    Text(
                        text = "Order #${state.orderId}",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorWhitePure,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Items List
            item {
                if (state.items.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        state.items.forEach { orderItem ->
                            OrderItemCard(orderItem = orderItem)
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Back to Home Button
            item {
                Button(
                    onClick = { viewModel.setEvent(OrderConfirmationEvent.OnBackToHomeClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorBluePrimary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Back to Home",
                        style = MaterialTheme.typography.titleMedium,
                        color = colorWhitePure
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun OrderItemCard(orderItem: OrderItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorBackgroundMain.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Item Icon
        Image(
            painter = androidx.compose.ui.res.painterResource(id = orderItem.item.iconResId),
            contentDescription = orderItem.item.title,
            modifier = Modifier.size(60.dp),
            contentScale = ContentScale.Fit
        )
        
        // Item Info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = orderItem.item.title,
                style = MaterialTheme.typography.titleMedium,
                color = colorWhitePure
            )
            Text(
                text = orderItem.item.description,
                style = MaterialTheme.typography.bodySmall,
                color = colorWhitePure.copy(alpha = 0.7f)
            )
        }
        
        // Quantity
        Box(
            modifier = Modifier
                .background(
                    color = colorWhitePure.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${orderItem.quantity}",
                style = MaterialTheme.typography.titleMedium,
                color = colorWhitePure
            )
        }
    }
}

