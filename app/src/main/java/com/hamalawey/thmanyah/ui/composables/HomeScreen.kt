package com.hamalawey.thmanyah.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.hamalawey.domain.model.Section

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    displayedSections: List<Section>,
    selectedContentType: String?,
    searchQuery: String,
    isLoading: Boolean,
    errorMessage: String?,
    allContentTypes: List<String>,
    onQueryChanged: (String) -> Unit,
    onFilterSelected: (String?) -> Unit,
    onRefreshData: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TopAppBarContent()

        SearchBar(
            searchQuery = searchQuery, onQueryChanged = onQueryChanged
        )

        if (searchQuery.isEmpty()) {
            CategoryTabs(
                categories = allContentTypes,
                selectedCategory = selectedContentType,
                onCategorySelected = onFilterSelected
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading && displayedSections.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.semantics {
                                contentDescription = "Loading indicator"
                            })
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                displayedSections.isEmpty() && searchQuery.isNotBlank() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No results found for \"$searchQuery\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                displayedSections.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No content available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    PullToRefreshBox(
                        isLoading, {
                            onRefreshData()
                        }) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(displayedSections) { section ->
                                SectionHeader(name = section.name)
                                SectionContent(section = section)
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontFamily = FontFamily.Serif
        ),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.onSurface
    )
}
