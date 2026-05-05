package com.dmb25.photogallerie.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmb25.photogallerie.R
import com.dmb25.photogallerie.data.model.MyGallery
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryLayout(viewModel: GaleryViewModel = koinViewModel()) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NoNextItem -> {
                    snackbarHostState.showSnackbar(
                        message = "Pas d'élément suivant",
                        duration = SnackbarDuration.Short
                    )
                }
                is UiEvent.NoPreviousItem -> {
                    snackbarHostState.showSnackbar(
                        message = "Pas d'élément précédent",
                        duration = SnackbarDuration.Short
                    )
                }

                else -> {}
            }
        }
    }

    GalleryLayoutContent(
        state = state.value,
        snackbarHostState = snackbarHostState,
        onNextClick = { id ->
            viewModel.getNextImage(id)
        }, onPrevClick = { id ->
            viewModel.getPreviousImage(id)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryLayoutContent(
    state: UiState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNextClick: (Int) -> Unit,
    onPrevClick: (Int) -> Unit) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,        // fond de la TopAppBar
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,   // couleur du titre
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary, // couleur icône nav
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary      // couleur icônes action
                ),
                title = { Text("Gallery") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.isError.isNotEmpty() -> {
                    Text(
                        text = state.isError,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    state.item?.let {
                        GalleryItem(it, onNextClick = { id ->
                            onNextClick(id)
                        }, onPrevClick = { id ->
                            onPrevClick(id)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryItem(item: MyGallery, onNextClick: (Int) -> Unit, onPrevClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(32.dp)
            ) {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        AuthorDetail(item)
        EndBar(id = item.id, onNextClick = {
            onNextClick(item.id)
        }, onPrevClick = {
            onPrevClick(item.id)
        })
    }
}

@Composable
fun AuthorImage(item: MyGallery, modifier: Modifier = Modifier) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Image(
            modifier = Modifier
                .padding(50.dp),
            painter = painterResource(id = item.image),
            contentDescription = null
        )
    }
}

@Composable
fun AuthorDetail(item: MyGallery) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Row() {
            Text(
                text = item.place,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "(${item.year})", color = MaterialTheme.colorScheme.onPrimaryContainer)
        }

    }
}

@Composable
fun EndBar(
    onPrevClick: (Int) -> Unit,
    onNextClick: (Int) -> Unit,
    id: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Button(
            onClick = { onPrevClick(id) },
        ) {
            Text(stringResource(R.string.prev_button))
        }
        Button(
            onClick = { onNextClick(id) },
        ) {
            Text(stringResource(R.string.next_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryLayoutPreview() {
    GalleryLayoutContent(
        state = UiState(
            item = MyGallery(
                id = 1,
                title = "Title",
                place = "Place",
                year = "2023",
                image = R.drawable.image1
            ),
            isLoading = false,
            isError = ""
        ),
        onNextClick = {},
        onPrevClick = {}
    )

}

@Preview(showBackground = true)
@Composable
fun AuthorImagePreview() {
    AuthorImage(
        item = MyGallery(
            id = 1,
            title = "Title",
            place = "Place",
            year = "2023",
            image = R.drawable.image1
        )
    )
}