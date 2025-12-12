package com.application.ui.feature_home_wrapper.cart.ui.feature_home_wrapper.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.R
import com.application.navigation.RootRoute
import com.application.ui.components.MenuButton
import com.application.ui.components.PrimaryButton
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBlack
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorGreen
import com.application.ui.theme.colorRed
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartInnerScreen(
    viewModel: CartInnerViewModel,
    navController: NavController,
    rootNavController: NavController? = null,
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is CartInnerEffect.NavigateToOrderConfirmation -> {
                    rootNavController?.navigate(RootRoute.OrderConfirmation.route) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    CartInnerScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onBackClick = { navController.popBackStack() },
        onEvent = { viewModel.setEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartInnerScreenContent(
    state: CartInnerState,
    onMenuClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onEvent: (CartInnerEvent) -> Unit = {}
) {
    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            TopAppBar(
                colors = topAppBarColors,
                title = { 
                    Text(
                        text = "Your Cart",
                        color = colorWhitePure
                    )
                },
                navigationIcon = {
                    MenuButton(onMenuClick)
                },
                actions = {
                    TextButton(onClick = onBackClick) {
                        Text(
                            text = "Back",
                            color = colorRed
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isEmpty) {
            EmptyCartContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Items list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.items) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            onIncreaseQuantity = { onEvent(CartInnerEvent.OnIncreaseQuantity(cartItem.item.id)) },
                            onDecreaseQuantity = { onEvent(CartInnerEvent.OnDecreaseQuantity(cartItem.item.id)) },
                            onRemoveItem = { onEvent(CartInnerEvent.OnRemoveItem(cartItem.item.id)) }
                        )
                    }
                }

                // Total and Confirm Order button
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL",
                            style = MaterialTheme.typography.titleMedium,
                            color = colorWhitePure
                        )
                        Text(
                            text = "$${String.format("%.2f", state.totalPrice)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = colorGreen
                        )
                    }

                    // Confirm Order button
                    PrimaryButton(
                        onClick = { onEvent(CartInnerEvent.OnConfirmOrder) },
                        text = "Confirm Order",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCartContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cart),
            contentDescription = "Empty cart",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(colorWhitePure.copy(alpha = 0.5f))
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No item in your cart",
            style = MaterialTheme.typography.bodyLarge,
            color = colorWhitePure.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemoveItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorBackgroundMain)
            .border(
                width = 1.dp,
                color = colorBluePrimary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Item image
            Image(
                painter = painterResource(id = cartItem.item.iconResId),
                contentDescription = cartItem.item.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorBluePrimary.copy(alpha = 0.1f))
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            // Item details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Title
                Text(
                    text = cartItem.item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorWhitePure
                )

                // Description
                Text(
                    text = cartItem.item.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorWhitePure.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quantity and Price row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quantity controls
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Decrease button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(colorWhitePure)
                                .clickableNoRipple { onDecreaseQuantity() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "-",
                                style = MaterialTheme.typography.titleMedium,
                                color = colorBlack,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Quantity display
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(colorWhitePure),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cartItem.quantity.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = colorBlack,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Increase button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(colorWhitePure)
                                .clickableNoRipple { onIncreaseQuantity() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                style = MaterialTheme.typography.titleMedium,
                                color = colorBlack,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Price
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(colorGreen)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$${String.format("%.2f", cartItem.totalPrice)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorBlack
                        )
                    }
                }
            }

            // Remove button
            IconButton(
                onClick = onRemoveItem,
                modifier = Modifier.size(32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Remove item",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(colorRed)
                )
            }
        }
    }
}

@Preview
@Composable
fun CartInnerScreenContentPreview() {
    AppTheme {
        CartInnerScreenContent(
            state = CartInnerState(),
            onMenuClick = {},
            onBackClick = {},
            onEvent = {}
        )
    }
}
