package com.google.developer.bugmaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.views.AnswerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements
        AnswerView.OnAnswerSelectedListener {

    private static final String TAG = QuizActivity.class.getSimpleName();

    //Number of quiz answers
    public static final int ANSWER_COUNT = 5;

    public static final String EXTRA_INSECTS = "insectList";
    public static final String EXTRA_ANSWER = "selectedInsect";

    private static final String SELECTED_ANSWER_KEY = "selected_answer_key";
    private static final String OPTIONS_KEY = "options_key";

    private TextView mQuestionText;
    private TextView mCorrectText;
    private AnswerView mAnswerSelect;

    private ArrayList<String> mOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionText = (TextView) findViewById(R.id.text_question);
        mCorrectText = (TextView) findViewById(R.id.text_correct);
        mAnswerSelect = (AnswerView) findViewById(R.id.answer_select);

        mAnswerSelect.setOnAnswerSelectedListener(this);

        List<Insect> insects = getIntent().getParcelableArrayListExtra(EXTRA_INSECTS);
        Insect selected = getIntent().getParcelableExtra(EXTRA_ANSWER);
        if (insects == null) {
            finish();
        }

        int checkedIndex = -1;
        if (savedInstanceState != null) {
            mOptions = savedInstanceState.getStringArrayList(OPTIONS_KEY);
            checkedIndex = savedInstanceState.getInt(SELECTED_ANSWER_KEY, -1);
        }

        buildQuestion(insects, selected);
        if (checkedIndex != -1) {
            mAnswerSelect.setCheckedIndex(checkedIndex);
            updateResultText();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ANSWER_KEY, mAnswerSelect.getCheckedIndex());
        outState.putStringArrayList(OPTIONS_KEY, mOptions);
    }

    private void buildQuestion(List<Insect> insects, Insect selected) {

        String question = getString(R.string.question_text, selected.name);
        mQuestionText.setText(question);

        //Load answer strings
        if (mOptions == null || mOptions.isEmpty()) {
            mOptions = new ArrayList<>();
            //add correct answer as a option
            mOptions.add(selected.scientificName);

            for (Insect item : insects) {
                //add more options
                if (!item.name.equals(selected.name)) {
                    mOptions.add(item.scientificName);
                }
                //limit the options
                if (mOptions.size() >= ANSWER_COUNT) {
                    break;
                }
            }
            //shuffle the options, so the correct answer is on a different place every time
            Collections.shuffle(mOptions);
        }
        mAnswerSelect.loadAnswers(mOptions, selected.scientificName);
    }

    /* Answer Selection Callbacks */

    @Override
    public void onCorrectAnswerSelected() {

        updateResultText();
    }

    @Override
    public void onWrongAnswerSelected() {

        updateResultText();
    }

    private void updateResultText() {

        mCorrectText.setTextColor(mAnswerSelect.isCorrectAnswerSelected() ?
                getColor(R.color.colorCorrect) : getColor(R.color.colorWrong)
        );
        mCorrectText.setText(mAnswerSelect.isCorrectAnswerSelected() ?
                R.string.answer_correct : R.string.answer_wrong
        );
    }
}
