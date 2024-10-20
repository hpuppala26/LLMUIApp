package com.example.llmapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText promptInput;
    private TextView responseText;
    private RadioButton geminiRadio;
    private Button sendButton;
    private Button cancelButton;

    private GeminiLLMservice geminiLLMService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promptInput = findViewById(R.id.promptInput);
        responseText = findViewById(R.id.responseText);
        geminiRadio = findViewById(R.id.geminiRadio);
        sendButton = findViewById(R.id.sendButton);
        cancelButton = findViewById(R.id.cancelButton);

        geminiLLMService = new GeminiLLMservice();

        geminiRadio.setChecked(true);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = promptInput.getText().toString();

                if (prompt.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a prompt", Toast.LENGTH_SHORT).show();
                    return;
                }

                responseText.setText("Loading...");

                geminiLLMService.generateContent(prompt, new GeminiLLMservice.ContentCallback() {
                    @Override
                    public void onSuccess(String content) {
                        runOnUiThread(() -> responseText.setText(content));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        runOnUiThread(() -> responseText.setText("Error: " + throwable.getMessage()));
                    }
                });
            }
        });

        cancelButton.setEnabled(false);
    }
}

