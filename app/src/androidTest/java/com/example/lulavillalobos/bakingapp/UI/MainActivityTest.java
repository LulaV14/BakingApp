package com.example.lulavillalobos.bakingapp.UI;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.lulavillalobos.bakingapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction recipesRecyclerView = onView(
                allOf(withId(R.id.rv_recipes),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recipesRecyclerView.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction ingredientsList = onView(
                allOf(withId(R.id.tv_ingredients_step), withText("Recipe Ingredients"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        ingredientsList.perform(scrollTo(), click());

        ViewInteraction ingredientQuantity = onView(
                allOf(withId(R.id.tv_quantity), withText("2.0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_ingredients_list),
                                        0),
                                1),
                        isDisplayed()));
        ingredientQuantity.check(matches(withText("2.0")));

        ViewInteraction ingredientMeasure = onView(
                allOf(withId(R.id.tv_measure), withText("CUP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_ingredients_list),
                                        0),
                                2),
                        isDisplayed()));
        ingredientMeasure.check(matches(withText("CUP")));

        ViewInteraction ingredientName = onView(
                allOf(withId(R.id.tv_ingredient_name), withText("Graham Cracker crumbs"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_ingredients_list),
                                        0),
                                3),
                        isDisplayed()));
        ingredientName.check(matches(withText("Graham Cracker crumbs")));

        pressBack();

        ViewInteraction recipeStepListRecyclerview = onView(
                allOf(withId(R.id.rv_step_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)));
        recipeStepListRecyclerview.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recipeStep = onView(withId(R.id.tv_step_description));
        recipeStep.check(matches(withText("Recipe Introduction")));

        ViewInteraction nextStepButton = onView(withId(R.id.btn_next_step));
        nextStepButton.check(matches(isDisplayed()));
        nextStepButton.check(matches(withText("Go To Next Step:\n Starting prep")));
        nextStepButton.perform(click());


        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction stepDescription = onView(withId(R.id.tv_step_description));
        stepDescription.check(matches(withText("1. Preheat the oven to 350°F. Butter a 9\" deep dish pie pan.")));

        ViewInteraction btnNextStep2 = onView(withId(R.id.btn_next_step));
        btnNextStep2.check(matches(isDisplayed()));

        ViewInteraction btnPreviousStep = onView(withId(R.id.btn_previous_step));
        btnPreviousStep.check(matches(isDisplayed()));


        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
