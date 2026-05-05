package com.dmb25.photogallerie.data.local

import com.dmb25.photogallerie.R
import com.dmb25.photogallerie.data.model.MyGallery

object DataSource {
    val galleryList = listOf(
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
        MyGallery(
            id = 3,
            title = "White Poodle in a Punt",
            author = "George Stubbs",
            year = "1780",
            imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-9.jpg"
        ),
        MyGallery(
            id = 4,
            title = "Self-Portrait",
            author = "Rembrandt van Rijn",
            year = "1659",
            imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-10.jpg"
        )
        ,
        MyGallery(
            id = 5,
            title = "Bacchus and Ariadne",
            author = "Giovanni Battista Tiepolo",
            year = "1696",
            imageUrl = "https://mymodernmet.com/wp/wp-content/uploads/2017/12/free-images-national-gallery-of-art-1.jpg"
        )
    )
}