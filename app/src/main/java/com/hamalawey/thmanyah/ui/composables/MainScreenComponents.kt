package com.hamalawey.thmanyah.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamalawey.thmanyah.R

@Composable
fun TopAppBarContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.thmanyah_black),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Welcome Back 👋", style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp
                    ), color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Thmanyah", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold, fontSize = 20.sp
                    ), color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        IconButton(onClick = { /* Handle notification click */ }) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String, onQueryChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(12.dp)
            )
            .height(56.dp)
            .semantics {
                contentDescription = "Search bar"
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp)
        )

        BasicTextField(
            value = searchQuery,
            onValueChange = onQueryChanged,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary, fontSize = 16.sp
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "What would you like to listen?",
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryTabs(
    categories: List<String>, selectedCategory: String?, onCategorySelected: (String?) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem(
            category = "For you",
            isSelected = selectedCategory == null,
            onClick = { onCategorySelected(null) })

        categories.forEach { categoryType ->
            val tabLabel = categoryType.replace("_", " ").split(" ")
                .joinToString(" ") { it.replaceFirstChar { first -> first.uppercase() } }

            TabItem(
                category = tabLabel,
                isSelected = selectedCategory == categoryType,
                onClick = { onCategorySelected(categoryType) })
        }
    }
}

@Composable
fun TabItem(
    category: String, isSelected: Boolean, onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = category,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(
                    if (isSelected) 30.dp else 0.dp
                )
                .height(3.dp)
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}
