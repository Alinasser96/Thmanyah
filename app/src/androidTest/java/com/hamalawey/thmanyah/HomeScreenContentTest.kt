package com.hamalawey.thmanyah

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hamalawey.domain.model.Section
import com.hamalawey.thmanyah.ui.composables.HomeScreenContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testSections = listOf(
        Section(
            name = "section 1",
            type = "square",
            contentType = "podcast",
            order = "1",
            content = listOf()
        ),
        Section(
            name = "section 2",
            type = "square",
            contentType = "podcast",
            order = "2",
            content = listOf()
        ),
    )

    private val testContentTypes = listOf("Type1", "Type2", "Type3")


    @Test
    fun whenLoading_showsProgressIndicator() {
        composeTestRule.setContent {
            HomeScreenContent(
                displayedSections = emptyList(),
                selectedContentType = null,
                searchQuery = "",
                isLoading = true,
                errorMessage = null,
                allContentTypes = testContentTypes,
                onQueryChanged = {},
                onFilterSelected = {},
                onRefreshData = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Loading indicator").assertExists()
    }

    @Test
    fun whenNoContent_showsEmptyMessage() {
        composeTestRule.setContent {
            HomeScreenContent(
                displayedSections = emptyList(),
                selectedContentType = null,
                searchQuery = "",
                isLoading = false,
                errorMessage = null,
                allContentTypes = testContentTypes,
                onQueryChanged = {},
                onFilterSelected = {},
                onRefreshData = {}
            )
        }

        composeTestRule.onNodeWithText("No content available").assertExists()
    }

    @Test
    fun whenHasSections_showsSectionHeaders() {
        composeTestRule.setContent {
            HomeScreenContent(
                displayedSections = testSections,
                selectedContentType = null,
                searchQuery = "",
                isLoading = false,
                errorMessage = null,
                allContentTypes = testContentTypes,
                onQueryChanged = {},
                onFilterSelected = {},
                onRefreshData = {}
            )
        }

        testSections.forEach { section ->
            composeTestRule.onNodeWithText(section.name).assertExists()
        }
    }

    @Test
    fun searchBar_isAlwaysVisible() {
        composeTestRule.setContent {
            HomeScreenContent(
                displayedSections = emptyList(),
                selectedContentType = null,
                searchQuery = "",
                isLoading = false,
                errorMessage = null,
                allContentTypes = testContentTypes,
                onQueryChanged = {},
                onFilterSelected = {},
                onRefreshData = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Search bar").assertExists()
    }

    @Test
    fun whenNoSearchQuery_categoryTabsAreVisible() {
        composeTestRule.setContent {
            HomeScreenContent(
                displayedSections = emptyList(),
                selectedContentType = null,
                searchQuery = "",
                isLoading = false,
                errorMessage = null,
                allContentTypes = testContentTypes,
                onQueryChanged = {},
                onFilterSelected = {},
                onRefreshData = {}
            )
        }

        testContentTypes.forEach { type ->
            composeTestRule.onNodeWithText(type).assertExists()
        }
    }

    @Test
    fun whenSearchQuery_categoryTabsAreHidden() {
        composeTestRule.setContent {
            HomeScreenContent(
                displayedSections = emptyList(),
                selectedContentType = null,
                searchQuery = "query",
                isLoading = false,
                errorMessage = null,
                allContentTypes = testContentTypes,
                onQueryChanged = {},
                onFilterSelected = {},
                onRefreshData = {}
            )
        }

        testContentTypes.forEach { type ->
            composeTestRule.onNodeWithText(type).assertDoesNotExist()
        }
    }
}