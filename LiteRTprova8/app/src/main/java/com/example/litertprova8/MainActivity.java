package com.example.litertprova8;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {



    private Button buttonMainActivity;
    private TextView introText;
    private OkHttpClient client;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonMainActivity = findViewById(R.id.buttonMainActivity);
        introText = findViewById(R.id.introText);
    }

    public void startDownload(View v){
        // Verifica la connessione di rete prima di avviare il download
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Assicurati di avere accesso a internet.", Toast.LENGTH_LONG).show();
            restartActivity();
            return; // Esci dal metodo se non c'è connessione
        }

        buttonMainActivity.setEnabled(false);
        introText.setText("Download in corso");
        download();

    }

    public void download(){

        final String URL = "https://raw.githubusercontent.com/AlexBoca0/LocalLiteRT/main/Student_depression.tflite"; // url indicato in maniera statica
        final String OUTPUT_PATH = getFilesDir() + "/model.tflite"; // path del modello scaricato indicato in maniera statica

        client = new OkHttpClient();

        Request request = new Request.Builder().url(URL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Errore nel download.", Toast.LENGTH_LONG).show();
                    restartActivity();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Errore nel download.", Toast.LENGTH_SHORT).show();
                        restartActivity();
                    });
                    return;
                }

                File file = new File(OUTPUT_PATH);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(response.body().bytes());
                }
                Log.d("Modello", "Modello scaricato con successo!");


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        introText.setText("Download completato");
                        buttonMainActivity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goToQuestionsActivity();
                            }
                        });
                        buttonMainActivity.setEnabled(true);
                        buttonMainActivity.setText("Avanti");
                    }
                });


            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public void goToQuestionsActivity() {
        Intent i = new Intent(this, QuestionsActivity.class);
        startActivity(i);
    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}