package com.google.developer.bugmaster;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by stoyan.yanev on 6.1.2018 г..
 */

@RunWith(AndroidJUnit4.class)
public class StartQuizActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickQuizFloatingButton() {

        //start quiz activity
        onView(withId(R.id.fab)).perform(click());

        //check quiz activity question view is displayed
        onView(withId(R.id.text_question)).check(matches(isDisplayed()));
    }
}
