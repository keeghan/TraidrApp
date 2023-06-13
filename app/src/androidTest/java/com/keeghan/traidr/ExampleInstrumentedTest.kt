package com.keeghan.traidr

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.keeghan.traidr.repository.ProductRepository
import com.keeghan.traidr.viewmodels.ProductViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ExampleInstrumentedTest {
//    @get:Rule(order = 0)
//    var hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createAndroidComposeRule<MainActivity>()
//
////    @get:Rule(order = 2)
////    val composeSingleRule = createComposeRule()
//
////    @Inject
////    lateinit var userRepository: UserRepository
//
//    @Inject
//    lateinit var productRepository:ProductRepository
//
//    lateinit var viewModel :ProductViewModel
//
//    @Before
//    fun setup() {
//        hiltRule.inject()
//        val context = ApplicationProvider.getApplicationContext<Context>()
//    }
//
//
//    @Test
//    fun testButtonClick() {
//        composeTestRule.onRoot().printToLog("currentLabelExists")
//        composeTestRule.onNode(
//            hasText("Sever needs a few calls to wake up"),
//            useUnmergedTree = true
//        ).assertExists()
//    }
//
//
//    @Test
//    fun testSomethingElse() {
//         composeTestRule.onNodeWithText("Email").performTextInput("1@gmail.com")
//        composeTestRule.onNodeWithText("Password").performTextInput("Androider69")
//        composeTestRule.onNodeWithText("Login").performClick()
//        composeTestRule.onRoot().printToLog("currentLabelExists")
//
////        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
////        composeTestRule.onNodeWithText("Password").assertExists()
////        composeTestRule.onNodeWithText("Login").assertExists()
//      //  Thread.sleep(5000)
//        composeTestRule.onNode(hasText("Home"), useUnmergedTree = true).assertIsDisplayed()
//    }

}