package com.example.llmapp;
import com.google.common.util.concurrent.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

public class GeminiLLMservice {

    private static final String MODEL_NAME = "gemini-1.5-flash";
    private static final String API_KEY = "AIzaSyBtrpILUZDW16heArNvSJWfzoMWZw7Uu6M";

    private final Executor executor = Executors.newSingleThreadExecutor();

    public void generateContent(String prompt, ContentCallback callback) {
        GenerativeModel gm = new GenerativeModel(MODEL_NAME, API_KEY);

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder().addText(prompt).build();

        ListenableFuture<GenerateContentResponse> responseFuture = model.generateContent(content);

        Futures.addCallback(
                responseFuture,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        callback.onSuccess(result.getText());
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        callback.onFailure(t);
                    }
                },
                executor
        );
    }

    public interface ContentCallback {
        void onSuccess(String content);

        void onFailure(Throwable throwable);
    }
}
