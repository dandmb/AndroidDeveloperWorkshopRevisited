package com.dmb25.photogallerie

import app.cash.turbine.test
import com.dmb25.photogallerie.data.model.MyGallery
import com.dmb25.photogallerie.data.repo.GalleryRepo
import com.dmb25.photogallerie.presentation.GaleryViewModel
import com.dmb25.photogallerie.presentation.UiEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before


@OptIn(ExperimentalCoroutinesApi::class)
class GaleryViewModelTest {

    private val galleryRepo = mockk<GalleryRepo>()
    private lateinit var viewModel: GaleryViewModel

    private val fakeList = listOf(
        MyGallery(
            id = 1,
            title = "Tropical Forest",
            author = "Henri Rousseau",
            year = "1844",
            imageUrl = "url1"
        ),
        MyGallery(id = 2, title = "Flowers", author = "Paul Cézanne", year = "1876", imageUrl = "url2"),
        MyGallery(id = 3, title = "Starry Night", author = "Van Gogh", year = "1889", imageUrl = "url3")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { galleryRepo.getGallery() } returns flow { emit(fakeList) }
        viewModel = GaleryViewModel(galleryRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getGallery sets isLoading to true then updates item`() = runTest {
        viewModel.uiState.test {
            val item = awaitItem()
            assertEquals(fakeList.first(), item.item)
            assertEquals(false, item.isLoading)
            assertEquals("", item.isError)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getGallery on error updates isError`() = runTest {
        coEvery { galleryRepo.getGallery() } returns flow { throw Exception("Network error") }
        viewModel = GaleryViewModel(galleryRepo)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isError.isNotEmpty())
            assertNull(state.item)
            assertEquals(false, state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getNextImage with valid id updates item`() = runTest {
        val nextItem = fakeList[1]
        coEvery { galleryRepo.getGalleryById(2) } returns nextItem

        viewModel.getNextImage(1)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(nextItem, state.item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getNextImage at last item emits NoNextItem`() = runTest {
        viewModel.uiEvent.test {
            viewModel.getNextImage(3)
            val event = awaitItem()
            assertEquals(UiEvent.NoNextItem, event)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPreviousImage with valid id updates item`() = runTest {
        val previousItem = fakeList[0]
        coEvery { galleryRepo.getGalleryById(1) } returns previousItem

        viewModel.getPreviousImage(2)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(previousItem, state.item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPreviousImage at first item emits NoPreviousItem`() = runTest {
        viewModel.uiEvent.test {
            viewModel.getPreviousImage(1)
            val event = awaitItem()
            assertEquals(UiEvent.NoPreviousItem, event)
            cancelAndIgnoreRemainingEvents()
        }
    }
}