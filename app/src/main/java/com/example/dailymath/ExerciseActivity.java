package com.example.dailymath;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExerciseActivity extends AppCompatActivity {

    private static final int TOTAL_QUESTIONS = 10;

    private TextView tvProgress, tvScore, tvQuestion;
    private EditText etAnswer;
    private Button btnNext;

    private TextView tvHint;

    private final Random random = new Random();
    private final List<Question> questions = new ArrayList<>();

    private int index = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        tvProgress = findViewById(R.id.tvProgress);
        tvScore = findViewById(R.id.tvScore);
        tvQuestion = findViewById(R.id.tvQuestion);
        etAnswer = findViewById(R.id.etAnswer);
        tvHint = findViewById(R.id.tvHint);
        btnNext = findViewById(R.id.btnNext);

        generateQuestions();
        showQuestion();

        // Allow pressing "Done" on the keyboard to behave like Next
        etAnswer.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onNextClicked();
                return true;
            }
            return false;
        });

        btnNext.setOnClickListener(v -> onNextClicked());
    }

    private void onNextClicked() {
        String input = etAnswer.getText().toString().trim();

        hideHint();
        if (TextUtils.isEmpty(input)) {
            //Toast.makeText(this, "Please enter a number.", Toast.LENGTH_SHORT).show();
            showHint("Please enter a number.");
            return;
        }

        int userAnswer;
        try {
            userAnswer = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // Should rarely happen because inputType=number, but safe to keep
            Toast.makeText(this, "Numbers only, please.", Toast.LENGTH_SHORT).show();
            return;
        }

        Question q = questions.get(index);
        if (userAnswer == q.correctAnswer) {
            score++;
            Toast.makeText(this, "Great job!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not quite. Answer: " + q.correctAnswer, Toast.LENGTH_SHORT).show();
        }

        index++;

        if (index >= TOTAL_QUESTIONS) {
            // Finished
            tvQuestion.setText("Completed!\nScore: " + score + " / " + TOTAL_QUESTIONS);
            tvProgress.setText("All done");
            tvScore.setText("Score: " + score);
            etAnswer.setText("");
            etAnswer.setEnabled(false);
            btnNext.setEnabled(false);
            btnNext.setText("Finished");
            return;
        }

        showQuestion();
    }

    private void showHint(String msg) {
        tvHint.setText(msg);
        tvHint.setVisibility(TextView.VISIBLE);
    }

    private void hideHint() {
        tvHint.setText("");
        tvHint.setVisibility(TextView.GONE);
    }

    private void showQuestion() {
        Question q = questions.get(index);

        tvProgress.setText("Question " + (index + 1) + " of " + TOTAL_QUESTIONS);
        tvScore.setText("Score: " + score);

        // Use × instead of *
        String symbol = q.opSymbol;
        tvQuestion.setText(q.a + " " + symbol + " " + q.b + " =");

        etAnswer.setText("");
        etAnswer.requestFocus();

        // Button text changes on last question
        if (index == TOTAL_QUESTIONS - 1) {
            btnNext.setText("Finish");
        } else {
            btnNext.setText("Next");
        }
    }

    private void generateQuestions() {
        questions.clear();

        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            questions.add(makeOneQuestion());
        }
    }

    private Question makeOneQuestion() {
        // Pick an operator: +, -, ×
        int opPick = random.nextInt(3);

        int a, b;
        String symbol;
        int answer;

        if (opPick == 0) {
            // Addition: 0..20
            a = random.nextInt(21);
            b = random.nextInt(21);
            symbol = "+";
            answer = a + b;

        } else if (opPick == 1) {
            // Subtraction: keep it non-negative (grandma-friendly)
            a = random.nextInt(21);
            b = random.nextInt(21);
            if (b > a) { int t = a; a = b; b = t; }
            symbol = "-";
            answer = a - b;

        } else {
            // Multiplication: smaller numbers (0..9)
            a = random.nextInt(10);
            b = random.nextInt(10);
            symbol = "×";
            answer = a * b;
        }

        return new Question(a, b, symbol, answer);
    }

    private static class Question {
        final int a;
        final int b;
        final String opSymbol;
        final int correctAnswer;

        Question(int a, int b, String opSymbol, int correctAnswer) {
            this.a = a;
            this.b = b;
            this.opSymbol = opSymbol;
            this.correctAnswer = correctAnswer;
        }
    }
}