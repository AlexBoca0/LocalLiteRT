package com.example.litertprova8;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;

public class ResultActivity extends AppCompatActivity {

    // Used to load the 'litertprova8' library on application startup.
    static {
        System.loadLibrary("litertprova8");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //cambia getint con getdouble
        float data1 = getIntent().getIntExtra("question1",0);
        float data2 = getIntent().getIntExtra("question2",0);
        data2 = (data2-18)/(34-18);
        float data3 = getIntent().getIntExtra("question3",0);
        data3 = data3/4;
        float data4 = getIntent().getIntExtra("question4",0);
        data4 = data4/4;
        float data5 = getIntent().getIntExtra("question5",0);
        data5 = data5/3;
        float data6 = getIntent().getIntExtra("question6",0);
        data6 = data6/2;
        float data7 = getIntent().getIntExtra("question7",0);
        float data8 = getIntent().getIntExtra("question8",0);
        data8 = data8/12;
        float data9 = getIntent().getIntExtra("question9",0);
        data9 = data9/4;
        float data10 = getIntent().getIntExtra("question10",0);
        float[] values = {data1, data2, data3, data4, data5, data6, data7, data8, data9, data10};

        float result = executeModel(getFilesDir() + "/model.tflite",values);

        deleteModel();

        TextView resultText = findViewById(R.id.resultText);
        String resultString = "";

        if(result<0.5){
            resultString = "Non sei a rischio depressione.";
            resultText.setTextColor(ContextCompat.getColor(this, R.color.green_200));
        } else {
            resultString = "Sei a rischio depressione.";
            resultText.setTextColor(ContextCompat.getColor(this, R.color.red_200));
        }
        String formattedResult = String.format("%.2f", result * 100);
        resultString+="\n(Percentuale depressione: "+formattedResult+"%)";
        resultText.setText(resultString);
    }

    public void restart(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void deleteModel() {
        File file = new File(getFilesDir() + "/model.tflite");
        if (file.exists()) {
            if (file.delete()) {
                Log.d("File", "Modello eliminato con successo!");
            } else {
                Log.e("File", "Errore nell'eliminazione del modello");
            }
        } else {
            Log.e("File", "Il modello non è stato trovato");
        }
    }


    /**
     * A native method that is implemented by the 'litertprova8' native library,
     * which is packaged with this application.
     */
    public native float executeModel(String path, float[] data);
}