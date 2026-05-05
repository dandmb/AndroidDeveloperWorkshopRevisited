package com.dmb25.practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dmb25.photogallerie.presentation.GalleryLayout
import com.dmb25.practice.ui.theme.PracticeTheme
import com.dmb25.tipsapp.ui.TipsLayout
import com.dmb25.tipsapp.ui.TipsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: TipsViewModel by viewModels()
        setContent {
            PracticeTheme {
//                TipsLayout(viewModel)

                GalleryLayout()
            }
        }
    }
}