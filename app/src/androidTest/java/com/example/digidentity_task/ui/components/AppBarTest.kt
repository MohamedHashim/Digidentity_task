package com.example.digidentity_task.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class AppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appBarDisplaysCorrectTitle() {
        val title = "Catalog"
        composeTestRule.setContent {
            AppBar(title = title, icon = Icons.Default.Home, iconClickAction = {})
        }

        composeTestRule
            .onNodeWithText(title)
            .assertExists()
    }

    @Test
    fun appBarDisplaysHomeIcon() {
        composeTestRule.setContent {
            AppBar(title = "Catalog", icon = Icons.Default.Home, iconClickAction = {})
        }

        composeTestRule
            .onNodeWithContentDescription("Home icon")
            .assertExists()
    }

    @Test
    fun appBarIconClickTriggersAction() {
        var clicked = false
        composeTestRule.setContent {
            AppBar(
                title = "Catalog",
                icon = Icons.Default.Home,
                iconClickAction = { clicked = true })
        }

        composeTestRule
            .onNodeWithContentDescription("Home icon")
            .performClick()

        assert(clicked)
    }
}