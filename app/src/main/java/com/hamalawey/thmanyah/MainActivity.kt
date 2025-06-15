package com.hamalawey.thmanyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hamalawey.thmanyah.ui.composables.HomeScreenContent
import com.hamalawey.thmanyah.ui.theme.ThmanyahTheme
import com.hamalawey.thmanyah.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThmanyahTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: MainViewModel = hiltViewModel()) {
    val displayedSections by viewModel.displayedSections.collectAsStateWithLifecycle()
    val selectedContentType by viewModel.selectedContentType.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val allContentTypes = remember(displayedSections) { viewModel.getAllContentTypes() }

    HomeScreenContent(
        displayedSections = displayedSections,
        selectedContentType = selectedContentType,
        searchQuery = searchQuery,
        isLoading = isLoading,
        errorMessage = errorMessage,
        allContentTypes = allContentTypes,
        onQueryChanged = {viewModel.onSearchQueryChanged(it)},
        onFilterSelected = {viewModel.selectContentType(it)},
        onRefreshData =  {viewModel.refreshData()}
    )

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ThmanyahTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
}