package cn.edu.sicnu.cardgame

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun editInformationIsShownOnMainScreen() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btn_edit_info)).perform(scrollTo(), click())
            onView(withId(R.id.et_name)).check(matches(isDisplayed()))

            onView(withId(R.id.et_name)).perform(replaceText("测试同学"), closeSoftKeyboard())
            onView(withId(R.id.btn_save)).perform(scrollTo(), click())

            onView(withId(R.id.tv_name_value)).check(matches(withText("测试同学")))
            onView(withId(R.id.tv_header_name)).check(matches(withText("测试同学")))
        }
    }

    @Test
    fun cancelEditKeepsOriginalInformation() {
        val originalName = context.getString(R.string.default_name)

        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btn_edit_info)).perform(scrollTo(), click())
            onView(withId(R.id.et_name)).perform(replaceText("不会保存的名字"), closeSoftKeyboard())
            onView(withId(R.id.btn_cancel)).perform(scrollTo(), click())

            onView(withId(R.id.tv_name_value)).check(matches(withText(originalName)))
        }
    }

    @Test
    fun playGameButtonOpensCardGame() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btn_play_game)).perform(scrollTo(), click())

            onView(withId(R.id.score_text)).check(
                matches(withText(context.getString(R.string.score_format, 0)))
            )
            onView(withId(R.id.btn_exit)).perform(click())
        }
    }
}
