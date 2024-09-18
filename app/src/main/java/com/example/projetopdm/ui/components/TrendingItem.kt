package com.example.projetopdm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.projetopdm.AppConstants
import com.example.projetopdm.model.TrendingItem

@Composable
fun TrendingItem(item: TrendingItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(120.dp, 180.dp)
            .background(Color.Black, shape = RoundedCornerShape(8.dp))
    ) {
        val posterUrl = AppConstants.TMDB_IMAGE_BASE_URL_ORIGINAL + item.poster_path
        Image(
            painter = rememberAsyncImagePainter(posterUrl),
            contentDescription = item.title ?: item.name ?: "Unknown",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
        )
    }
}