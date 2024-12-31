package ir.fallahpoor.eks.libraries.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

fun ComposeContentTestRule.clickOnNodeWithText(text: String) {
    onNodeWithText(text, useUnmergedTree = true).performClick()
}

fun ComposeContentTestRule.clickOnNodeWithTag(tag: String) {
    onNodeWithTag(tag, useUnmergedTree = true).performClick()
}

fun ComposeContentTestRule.clickOnNodeWithContentDescription(contentDescription: String) {
    onNodeWithContentDescription(contentDescription, useUnmergedTree = true).performClick()
}

fun ComposeContentTestRule.assertTextIsDisplayed(text: String) {
    onNodeWithText(text, useUnmergedTree = true).assertIsDisplayed()
}

fun ComposeContentTestRule.assertTextDoesNotExist(text: String) {
    onNodeWithText(text, useUnmergedTree = true).assertDoesNotExist()
}

fun ComposeContentTestRule.assertIsDisplayedNodeWithTag(tag: String) {
    onNodeWithTag(tag, useUnmergedTree = true).assertIsDisplayed()
}

fun ComposeContentTestRule.assertDoesNotExistNodeWithTag(tag: String) {
    onNodeWithTag(tag, useUnmergedTree = true).assertDoesNotExist()
}

fun ComposeContentTestRule.assertIsDisplayedNodeWithContentDescription(tag: String) {
    onNodeWithContentDescription(tag, useUnmergedTree = true).assertIsDisplayed()
}