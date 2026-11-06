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
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun clearSavedData() {
        GameStorage.clearAll(context)
    }

    @Test
    fun bottomNavigationShowsThreeFragments() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.tv_name_value))
                .check(matches(withText(context.getString(R.string.default_name))))

            onView(withId(R.id.nav_game_two)).perform(click())
            onView(withId(R.id.score_text)).check(
                matches(withText(context.getString(R.string.score_format, 0)))
            )

            onView(withId(R.id.nav_game_three)).perform(click())
            onView(withId(R.id.score_text)).check(
                matches(withText(context.getString(R.string.score_format, 0)))
            )
        }
    }

    @Test
    fun gameProgressIsRestoredAfterSwitchingFragment() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.nav_game_two)).perform(click())
            onView(withContentDescription(context.getString(R.string.card_description, 1)))
                .perform(click())
            onView(withId(R.id.score_text)).check(
                matches(withText(context.getString(R.string.score_format, -1)))
            )

            onView(withId(R.id.nav_profile)).perform(click())
            onView(withId(R.id.nav_game_two)).perform(click())

            onView(withId(R.id.tv_save_state)).check(
                matches(withText(context.getString(R.string.state_restored)))
            )
            onView(withId(R.id.score_text)).check(
                matches(withText(context.getString(R.string.score_format, -1)))
            )
        }
    }

    @Test
    fun editedProfileIsRestoredAfterReopen() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btn_edit_info)).perform(scrollTo(), click())
            onView(withId(R.id.et_name)).perform(replaceText("测试同学"), closeSoftKeyboard())
            onView(withId(R.id.btn_save)).perform(scrollTo(), click())
            onView(withId(R.id.tv_name_value)).check(matches(withText("测试同学")))
        }

        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.tv_name_value)).check(matches(withText("测试同学")))
        }
    }
}
