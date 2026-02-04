package com.example.kori.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.kori.data.local.model.Dish
import com.example.kori.viewmodel.DishViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: DishViewModel = viewModel(),
    onNavigateToMenu: () -> Unit
) {
    val dishes by viewModel.allDishes.collectAsState()
    val bestsellers by viewModel.bestsellers.collectAsState()
    var selectedCategory by remember { mutableStateOf("ITEM1") }
    
    val categories = listOf("ITEM1", "ITEM2", "ITEM3")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        
        // 🎯 HERO SECTION (como tus capturas)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Imagen hero
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://picsum.photos/800/400?blur")
                        .crossfade(true)
                        .build(),
                    contentDescription = "KÖRI Restaurant",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Overlay con título + botones
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "KÖRI",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC9A86A),  // Dorado KÖRI
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row {
                            Button(
                                onClick = onNavigateToMenu,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC9A86A))
                            ) {
                                Text("Menú", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            OutlinedButton(onClick = { /* Reservas */ }) {
                                Text("Reservar")
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // 🎯 CATEGORÍAS HORIZONTAL (LazyRow como tus capturas)
        Text(
            text = "Menú",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC9A86A),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (selectedCategory == category) 
                            Color(0xFFC9A86A) else Color.Transparent,
                        labelColor = Color.White
                    )
                )
            }
        }
        
        // Platos por categoría
        val categoryDishes by viewModel.getDishesByCategory(selectedCategory).collectAsState()
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.height(220.dp)
        ) {
            items(categoryDishes) { dish ->
                DishCard(dish = dish, onToggleFavorite = {
                    viewModel.toggleFavorite(it)
                })
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // 🎯 BESTSELLERS GRID (como tus capturas)
        Text(
            text = "Bestsellers",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC9A86A),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(bestsellers) { dish ->
                DishCardSmall(dish = dish, onToggleFavorite = {
                    viewModel.toggleFavorite(it)
                })
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun DishCard(
    dish: Dish,
    onToggleFavorite: (Dish) -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { },
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            // Imagen
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = dish.name,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            
            // Info
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = dish.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC9A86A),
                    maxLines = 1
                )
                Text(
                    text = "€${dish.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            // Botón favorito
            IconButton(
                onClick = { onToggleFavorite(dish) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = Color(0xFFC9A86A),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun DishCardSmall(
    dish: Dish,
    onToggleFavorite: (Dish) -> Unit
) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = dish.name,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = dish.name,
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp),
                maxLines = 1
            )
        }
    }
}
