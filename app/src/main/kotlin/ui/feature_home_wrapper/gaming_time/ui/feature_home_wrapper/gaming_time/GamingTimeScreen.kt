package com.application.ui.feature_home_wrapper.gaming_time.ui.feature_home_wrapper.gaming_time

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.R
import com.application.domain.model.ItemCategory
import com.application.navigation.HomeRoute
import com.application.ui.components.MenuButton
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBlack
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorGreen
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamingTimeScreen(
    viewModel: GamingTimeViewModel,
    navController: NavController,
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is GamingTimeEffect.NavigateToCart -> {
                    navController.navigate(HomeRoute.CartInner.route) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    GamingTimeScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onCartIconClick = { viewModel.setEvent(GamingTimeEvent.OnCartIconClicked) },
        onEvent = { viewModel.setEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamingTimeScreenContent(
    state: GamingTimeState,
    onMenuClick: () -> Unit = {},
    onCartIconClick: () -> Unit = {},
    onEvent: (GamingTimeEvent) -> Unit = {}
) {
    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            TopAppBar(
                colors = topAppBarColors,
                title = { },
                navigationIcon = {
                    MenuButton(onMenuClick)
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        IconButton(onClick = onCartIconClick) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_cart),
                                contentDescription = "Cart",
                                modifier = Modifier.size(36.dp),
                                contentScale = ContentScale.Fit,
                            )
                        }
                        if (state.cartItemCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        color = colorBluePrimary,
                                        shape = CircleShape
                                    )
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.cartItemCount.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colorWhitePure,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category filters
            CategoryFilters(
                selectedCategory = state.selectedCategory,
                onCategorySelected = { category ->
                    onEvent(GamingTimeEvent.OnCategorySelected(category))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Items grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                    items(state.filteredItems) { item ->
                        ItemCard(
                            item = item,
                            onAddToCart = { onEvent(GamingTimeEvent.OnAddToCart(item)) }
                        )
                    }
            }
        }
    }
}

@Composable
fun CategoryFilters(
    selectedCategory: ItemCategory?,
    onCategorySelected: (ItemCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryChip(
            text = "All",
            isSelected = selectedCategory == null,
            onClick = { onCategorySelected(null) }
        )
        CategoryChip(
            text = "PC Time",
            isSelected = selectedCategory == ItemCategory.PC_TIME,
            onClick = { onCategorySelected(ItemCategory.PC_TIME) }
        )
        CategoryChip(
            text = "Console Time",
            isSelected = selectedCategory == ItemCategory.CONSOLE_TIME,
            onClick = { onCategorySelected(ItemCategory.CONSOLE_TIME) }
        )
        CategoryChip(
            text = "Drinks",
            isSelected = selectedCategory == ItemCategory.DRINKS,
            onClick = { onCategorySelected(ItemCategory.DRINKS) }
        )
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickableNoRipple { onClick() }
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = colorBluePrimary,
                        shape = RoundedCornerShape(20.dp)
                    )
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) colorBluePrimary else colorWhitePure.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ItemCard(
    item: GamingTimeItem,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colorBackgroundMain)
            .border(
                width = 1.dp,
                color = colorBluePrimary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title and Icon row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title on the left
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorWhitePure,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                
                // Icon on the right
                Image(
                    painter = painterResource(id = item.iconResId),
                    contentDescription = item.title,
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Description
            Text(
                text = item.description,
                style = MaterialTheme.typography.labelSmall,
                color = colorWhitePure.copy(alpha = 0.7f),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Price and Add button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price in green rectangle - black text, smaller size
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorGreen)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$${String.format("%.2f", item.price)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorBlack
                    )
                }

                // Add to cart button - transparent, blue text and icon
                Row(
                    modifier = Modifier
                        .clickableNoRipple { onAddToCart() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add to",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorBluePrimary
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_cart),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(colorBluePrimary)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GamingTimeScreenContentPreview() {
    AppTheme {
        GamingTimeScreenContent(
            state = GamingTimeState(),
            onMenuClick = {},
            onCartIconClick = {},
            onEvent = {}
        )
    }
}

