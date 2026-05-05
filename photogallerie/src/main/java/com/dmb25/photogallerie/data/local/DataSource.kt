package com.dmb25.photogallerie.data.local

import com.dmb25.photogallerie.R
import com.dmb25.photogallerie.data.model.MyGallery

object DataSource {
    val galleryList = listOf(
        MyGallery(
            id = 1,
            title = "DMB",
            place = "Kinshasa",
            year = "2022",
            image = R.drawable.image1
        ),
        MyGallery(
            id = 2,
            title = "DAN",
            place = "Paris",
            year = "2020",
            image = R.drawable.image2
        ),
        MyGallery(
            id = 3,
            title = "BIZWA",
            place = "Tokyo",
            year = "2023",
            image = R.drawable.image3
        ),
        MyGallery(
            id = 4,
            title = "MBWEY",
            place = "New York",
            year = "2021",
            image = R.drawable.image4
        )
    )
}