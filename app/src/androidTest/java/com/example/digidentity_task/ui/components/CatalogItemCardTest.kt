package com.example.digidentity_task.ui.components


import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.digidentity_task.model.CatalogItem
import org.junit.Rule
import org.junit.Test

class CatalogItemCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun catalogItemCard_DisplaysCorrectText() {
        val catalogItem =
            CatalogItem("Sample Text", 0.99f, "https://example.com/image.png", "123456")

        composeTestRule.setContent {
            CatalogItemCard(catalogItem = catalogItem) {}
        }

        composeTestRule
            .onNodeWithText("Sample Text")
            .assertIsDisplayed()
    }

    @Test
    fun catalogItemCard_DisplaysCorrectConfidence() {
        val catalogItem =
            CatalogItem("Sample Text", 0.99f, "https://example.com/image.png", "123456")

        composeTestRule.setContent {
            CatalogItemCard(catalogItem = catalogItem) {}
        }

        composeTestRule
            .onNodeWithText("0.99")
            .assertIsDisplayed()
    }

    @Test
    fun catalogItemCard_DisplaysCorrectId() {
        val catalogItem =
            CatalogItem("Sample Text", 0.99f, "https://example.com/image.png", "123456")

        composeTestRule.setContent {
            CatalogItemCard(catalogItem = catalogItem) {}
        }

        composeTestRule
            .onNodeWithText("123456")
            .assertIsDisplayed()
    }
}