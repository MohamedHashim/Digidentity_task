package com.example.digidentity_task.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.unit.dp
import com.example.digidentity_task.ui.components.CatalogItemImage
import org.junit.Rule
import org.junit.Test

class CatalogItemImageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun catalogItemImage_DisplaysCorrectly() {
        val testImageUrl = "https://example.com/test-image.png"
        val imageSize = 100.dp

        composeTestRule.setContent {
            CatalogItemImage(pictureUrl = testImageUrl, imageSize = imageSize)
        }

        composeTestRule
            .onNodeWithContentDescription("Catalog item description")
            .assertIsDisplayed()
    }
}