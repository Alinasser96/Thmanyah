package com.hamalawey.thmanyah
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import app.cash.turbine.test
import com.hamalawey.domain.model.Episode
import com.hamalawey.domain.model.Podcast
import com.hamalawey.domain.model.Section
import com.hamalawey.domain.repository.AppRepository
import com.hamalawey.thmanyah.viewmodel.MainViewModel

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val mockRepository: AppRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private val mockPodcastSection = Section(
        name = "Top Podcasts",
        type = "square",
        contentType = "podcast",
        order = "1",
        content = listOf(
            Podcast(
                id = "1", name = "Podcast 1", description = "Desc 1", avatarUrl = "url1",
                episodeCount = 10, duration = 60000, language = "en",
                priority = "1", popularityScore = "5", score = "100.0"
            )
        )
    )

    private val mockEpisodeSection = Section(
        name = "Trending Episodes",
        type = "2_lines_grid",
        contentType = "episode",
        order = "2",
        content = listOf(
            Episode(
                id = "e1", name = "Episode 1", description = "Ep Desc 1", avatarUrl = "epurl1",
                podcastName = "Pod 1", authorName = "Author 1", duration = 1200, score = "50.0"
            )
        )
    )

    private val mockAllHomeSections = listOf(mockPodcastSection, mockEpisodeSection)
    private val mockOnlyPodcastSections = listOf(mockPodcastSection)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private suspend fun mockDefaultRepositoryResponses() {
        coEvery { mockRepository.getHomeSections() } returns Result.success(mockAllHomeSections)
        coEvery { mockRepository.searchContent(any(), any(), any(), any()) } returns Result.success(emptyList())
    }

    @Test
    fun `init loads home sections and displays them filtered by default (All)`() = runTest {
        // 1. Set up mocks
        mockDefaultRepositoryResponses()
        // 2. Initialize ViewModel AFTER mocks are set
        viewModel = MainViewModel(mockRepository)
        // 3. Advance time for ViewModel's init block coroutines to complete
        advanceUntilIdle()

        // Verify initial state
        assertThat(viewModel.isLoading.first()).isFalse()
        assertThat(viewModel.errorMessage.first()).isNull()
        assertThat(viewModel.displayedSections.first()).isEqualTo(mockAllHomeSections)
        assertThat(viewModel.selectedContentType.first()).isNull()

        // Verify repository interaction
        coVerify(exactly = 1) { mockRepository.getHomeSections() }
        coVerify(exactly = 0) { mockRepository.searchContent(any(), any(), any(), any()) }
    }

    @Test
    fun `loadHomeSectionsAndDisplay updates loading and error states on failure`() = runTest {
        val errorMessage = "Initial load failed"
        // 1. Set specific mock for this test's scenario
        coEvery { mockRepository.getHomeSections() } returns Result.failure(RuntimeException(errorMessage))

        // 2. Initialize ViewModel after its mocks
        viewModel = MainViewModel(mockRepository)
        // 3. Advance time to allow initial coroutines to complete
        advanceUntilIdle()

        // Verify states
        assertThat(viewModel.isLoading.first()).isFalse()
        assertThat(viewModel.errorMessage.first()).isEqualTo("Error fetching home sections: $errorMessage")
        assertThat(viewModel.displayedSections.first()).isEmpty()
    }

    @Test
    fun `onSearchQueryChanged updates searchQuery immediately and triggers debounced search`() = runTest {
        // 1. Set up mocks
        mockDefaultRepositoryResponses()
        // 2. Initialize ViewModel
        viewModel = MainViewModel(mockRepository)
        // 3. Ensure ViewModel is ready and initial load finished before starting interaction
        advanceUntilIdle()

        viewModel.searchQuery.test {
            assertThat(awaitItem()).isEqualTo("") // Consume initial empty query

            // Mock search response for this specific query
            val searchResultSection = mockPodcastSection.copy(name = "Search Results for 'NPR'", contentType = "podcast")
            coEvery { mockRepository.searchContent(eq("NPR"), isNull(), isNull(), isNull()) } returns Result.success(listOf(searchResultSection))

            viewModel.onSearchQueryChanged("NPR") // Trigger search query change
            assertThat(awaitItem()).isEqualTo("NPR") // UI's searchQuery updates immediately

            coVerify(exactly = 0) { mockRepository.searchContent(any(), any(), any(), any()) } // No search API call yet

            advanceTimeBy(200) // Advance by debounce time
            advanceUntilIdle() // Ensure all pending coroutines run

            coVerify(exactly = 1) { mockRepository.searchContent(eq("NPR"), isNull(), isNull(), isNull()) } // Verify search was called
            assertThat(viewModel.isLoading.first()).isFalse()
            assertThat(viewModel.displayedSections.first()).isEqualTo(listOf(searchResultSection)) // Verify displayed sections updated

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onSearchQueryChanged debounce prevents multiple search API calls`() = runTest {
        mockDefaultRepositoryResponses()
        viewModel = MainViewModel(mockRepository)
        advanceUntilIdle()

        viewModel.searchQuery.test {
            assertThat(awaitItem()).isEqualTo("")

            viewModel.onSearchQueryChanged("a")
            assertThat(awaitItem()).isEqualTo("a")
            advanceTimeBy(100)
            viewModel.onSearchQueryChanged("ab")
            assertThat(awaitItem()).isEqualTo("ab")
            advanceTimeBy(100)
            viewModel.onSearchQueryChanged("abc")
            assertThat(awaitItem()).isEqualTo("abc")
            advanceTimeBy(199)

            coVerify(exactly = 0) { mockRepository.searchContent(any(), any(), any(), any()) }

            advanceTimeBy(1)
            advanceUntilIdle()

            coVerify(exactly = 1) { mockRepository.searchContent(eq("abc"), isNull(), isNull(), isNull()) }

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onSearchQueryChanged clears query, reverts to filtered home sections immediately`() = runTest {
        mockDefaultRepositoryResponses()
        viewModel = MainViewModel(mockRepository)
        advanceUntilIdle()

        val searchResult = listOf(mockPodcastSection.copy(name = "Search Result", contentType = "podcast"))
        coEvery { mockRepository.searchContent(eq("test"), any(), any(), any()) } returns Result.success(searchResult)

        viewModel.displayedSections.test {
            assertThat(awaitItem()).isEqualTo(mockAllHomeSections) // Initial state

            viewModel.onSearchQueryChanged("test")
            advanceTimeBy(200)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(searchResult) // Verify it changed to search result

            viewModel.onSearchQueryChanged("")
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(mockAllHomeSections) // Verify it reverted to home sections

            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) { mockRepository.getHomeSections() }
        coVerify(exactly = 1) { mockRepository.searchContent(eq("test"), any(), any(), any()) }
    }

    @Test
    fun `selectContentType filters home sections immediately when search is inactive`() = runTest {
        mockDefaultRepositoryResponses()
        viewModel = MainViewModel(mockRepository)
        advanceUntilIdle()
        assertThat(viewModel.displayedSections.first()).isEqualTo(mockAllHomeSections)
        assertThat(viewModel.selectedContentType.first()).isNull()

        viewModel.selectContentType("podcast")
        advanceUntilIdle()

        assertThat(viewModel.selectedContentType.first()).isEqualTo("podcast")
        assertThat(viewModel.displayedSections.first()).isEqualTo(mockOnlyPodcastSections)
        coVerify(exactly = 0) { mockRepository.searchContent(any(), any(), any(), any()) }
        coVerify(exactly = 1) { mockRepository.getHomeSections() }
    }

    @Test
    fun `getAllContentTypes returns distinct content types from base home sections`() = runTest {
        mockDefaultRepositoryResponses()
        viewModel = MainViewModel(mockRepository)
        advanceUntilIdle()
        val contentTypes = viewModel.getAllContentTypes()
        assertThat(contentTypes).containsExactly("podcast", "episode").inOrder()

        viewModel.onSearchQueryChanged("test")
        advanceUntilIdle()
        val contentTypesDuringSearch = viewModel.getAllContentTypes()
        assertThat(contentTypesDuringSearch).containsExactly("podcast", "episode").inOrder()

        viewModel.selectContentType("podcast")
        advanceUntilIdle()
        val contentTypesFilteredHome = viewModel.getAllContentTypes()
        assertThat(contentTypesFilteredHome).containsExactly("podcast", "episode").inOrder()
    }
}