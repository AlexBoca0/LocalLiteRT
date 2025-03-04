package com.example.litertprova8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

public class QuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
    }

    public void goToResult(View v){
        Intent i = new Intent(this, ResultActivity.class);

        int answer1 = findRadioOptionByGroup(findViewById(R.id.answer1));

        String answer2string = ((EditText) findViewById(R.id.answer2)).getText().toString();
        int answer2;
        if(!answer2string.isBlank()){
            answer2 = Integer.parseInt( answer2string );
        } else {
            answer2 = -1;
        }

        int answer3 = ( (SeekBar) findViewById(R.id.answer3) ).getProgress();
        int answer4 = ( (SeekBar) findViewById(R.id.answer4) ).getProgress();
        int answer5 = findRadioOptionByGroup(findViewById(R.id.answer5));
        int answer6 = findRadioOptionByGroup(findViewById(R.id.answer6));
        int answer7 = findRadioOptionByGroup(findViewById(R.id.answer7));
        int answer8 = ( (SeekBar) findViewById(R.id.answer8) ).getProgress();
        int answer9 = ( (SeekBar) findViewById(R.id.answer9) ).getProgress();
        int answer10 = findRadioOptionByGroup(findViewById(R.id.answer10));
        int[] answers = {answer1, answer2, answer5, answer6, answer7, answer10};
        if(Arrays.stream(answers).anyMatch(n -> n==-1)){
            Toast.makeText(this, "Rispondere a tutte le domande", Toast.LENGTH_SHORT).show();
            return;
        }
        i.putExtra("question1", answer1);
        i.putExtra("question2", answer2);
        i.putExtra("question3", answer3);
        i.putExtra("question4", answer4);
        i.putExtra("question5", answer5);
        i.putExtra("question6", answer6);
        i.putExtra("question7", answer7);
        i.putExtra("question8", answer8);
        i.putExtra("question9", answer9);
        i.putExtra("question10", answer10);

        startActivity(i);
    }

    private int findRadioOptionByGroup(RadioGroup group){
        int checkedId = group.getCheckedRadioButtonId();
        if (checkedId != -1) {
            RadioButton selectedRadioButton = findViewById(checkedId);
            return Integer.parseInt(selectedRadioButton.getTag().toString());
        } else {
            return -1;
        }
    }
}