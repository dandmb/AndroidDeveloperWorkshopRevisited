package com.dmb25.photogallerie

import app.cash.turbine.test
import com.dmb25.photogallerie.data.local.DataSource
import com.dmb25.photogallerie.data.model.MyGallery
import com.dmb25.photogallerie.data.repo.GalleryRepoImpl
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GalleryRepoImplTest {

    private lateinit var repo: GalleryRepoImpl
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    @Before
    fun setup() {
        mockkObject(DataSource)
        repo = GalleryRepoImpl(DataSource, testDispatcher)
    }

    @After
    fun tearDown() {
        unmockkObject(DataSource)
    }


    @Test
    fun `getGallery emits the gallery list`() = runTest(testScheduler) {
        val fakeList = listOf(
            MyGallery(
                id = 1,
                title = "Tropical Forest with Monkeys",
                author = "Henri Rousseau",
                year = "1844 – 1910",
                imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-3.jpg"
            ),
            MyGallery(
                id = 2,
                title = "Flowers in a Rococo Vase",
                author = "Paul Cézanne",
                year = "1876",
                imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-8.jpg"
            ),
        )
        every { DataSource.galleryList } returns fakeList

        repo.getGallery().test {
            val result = awaitItem()
            assertEquals(fakeList, result)
            awaitComplete()
        }
    }

    @Test
    fun `getGallery emits list with correct size`() = runTest(testScheduler) {
        val fakeList = listOf(
            MyGallery(
                id = 1,
                title = "Tropical Forest with Monkeys",
                author = "Henri Rousseau",
                year = "1844 – 1910",
                imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-3.jpg"
            ),
            MyGallery(
                id = 2,
                title = "Flowers in a Rococo Vase",
                author = "Paul Cézanne",
                year = "1876",
                imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-8.jpg"
            ),
        )
        every { DataSource.galleryList } returns fakeList

        repo.getGallery().test {
            val result = awaitItem()
            assertEquals(2, result.size)
            awaitComplete()
        }
    }


    @Test
    fun `getGalleryById returns correct item when id exists`() = runTest {
        val fakeList = listOf(
            MyGallery(
                id = 1,
                title = "Tropical Forest with Monkeys",
                author = "Henri Rousseau",
                year = "1844 – 1910",
                imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-3.jpg"
            ),
            MyGallery(
                id = 2,
                title = "Flowers in a Rococo Vase",
                author = "Paul Cézanne",
                year = "1876",
                imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-8.jpg"
            ),
        )
        every { DataSource.galleryList } returns fakeList

        val result = repo.getGalleryById(1)

        assertEquals(fakeList[0], result)
    }

    @Test
    fun `getGalleryById returns null when id does not exist`() = runTest {
        val fakeList = listOf(
            MyGallery(
                id = 1,
                title = "Tropical Forest with Monkeys",
                author = "Henri Rousseau",
                year = "1844 – 1910",
                imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-3.jpg"
            ),
            MyGallery(
                id = 2,
                title = "Flowers in a Rococo Vase",
                author = "Paul Cézanne",
                year = "1876",
                imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-8.jpg"
            ),
        )
        every { DataSource.galleryList } returns fakeList

        val result = repo.getGalleryById(99)

        assertNull(result)
    }

    @Test
    fun `getGalleryById returns null when list is empty`() = runTest {
        every { DataSource.galleryList } returns emptyList()

        val result = repo.getGalleryById(1)

        assertNull(result)
    }
}