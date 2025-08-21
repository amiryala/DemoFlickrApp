package com.lotus.demoflickrapp.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.lotus.demoflickrapp.domain.model.Photo

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun PhotoDetailScreen(
    photo: Photo,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = photo.title.ifBlank { "Photo Detail" },
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Full size image with original aspect ratio
            GlideImage(
                model = photo.url.replace("_w", "_b"), // Use large size (1024px) for detail view
                contentDescription = photo.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                contentScale = ContentScale.Fit
            )
            
            // Photo information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (photo.title.isNotBlank()) {
                    Text(
                        text = photo.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Divider()
                
                PhotoInfoRow(label = "Photo ID", value = photo.id)
                PhotoInfoRow(label = "Owner", value = photo.owner)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PhotoBadge(
                        label = if (photo.isPublic == 1) "Public" else "Private",
                        isHighlighted = photo.isPublic == 1
                    )
                    PhotoBadge(
                        label = if (photo.isFriend == 1) "Friend" else "Not Friend",
                        isHighlighted = photo.isFriend == 1
                    )
                    PhotoBadge(
                        label = if (photo.isFamily == 1) "Family" else "Not Family",
                        isHighlighted = photo.isFamily == 1
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PhotoBadge(
    label: String,
    isHighlighted: Boolean
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (isHighlighted) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (isHighlighted) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}