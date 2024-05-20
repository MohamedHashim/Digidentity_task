package com.example.digidentity_task.ui.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.tooling.preview.Preview
import org.junit.Rule
import org.junit.Test

class LoadingIndicatorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingIndicator_IsDisplayed() {
        composeTestRule.setContent {
            LoadingIndicator()
        }

        composeTestRule
            .onNodeWithTag("loadingIndicator")
            .assertIsDisplayed()
    }
}