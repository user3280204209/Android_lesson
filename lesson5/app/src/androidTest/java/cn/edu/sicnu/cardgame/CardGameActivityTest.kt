package cn.edu.sicnu.cardgame

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardGameActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun choosingCardCostsOnePointAndResetRestoresZero() {
        ActivityScenario.launch(CardGameActivity::class.java).use {
            onView(withId(R.id.score_text)).check(
                matches(withText(context.getString(R.string.score_format, 0)))
            )

            val firstCard = context.getString(R.string.card_description, 1)
            onView(withContentDescription(firstCard)).perform(click())
            onView(withId(R.id.score_text)).check(
                matches(withText(context.getString(R.string.score_format, -1)))
            )

            onView(withId(R.id.btn_reset)).perform(click())
            onView(withId(R.id.score_text)).check(
                matches(withText(context.getString(R.string.score_format, 0)))
            )
        }
    }

    @Test
    fun firstCardsShowBackSideBeforeBeingChosen() {
        ActivityScenario.launch(CardGameActivity::class.java).use {
            val back = context.getString(R.string.card_back)
            for (index in 1..4) {
                onView(withContentDescription(context.getString(R.string.card_description, index)))
                    .check(matches(withText(back)))
            }
        }
    }
}
