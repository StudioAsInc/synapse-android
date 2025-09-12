package com.synapse.social.studioasinc.AI;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.firebase.ai.client.generativeai.type.Content;
import com.google.firebase.ai.client.generativeai.type.GenerateContentResponse;
import com.google.firebase.ai.client.generativeai.type.GenerationConfig;
import com.google.firebase.ai.client.generativeai.java.FirebaseGenerativeAI;
import com.synapse.social.studioasinc.R;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Gemini {

    private static final String TAG = "GeminiAPI";

    // Default configuration
    private final GenerativeModelFutures generativeModel;
    private final boolean showThinking;
    private final String thinkingText;
    private final TextView responseTextView;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public interface GeminiCallback {
        void onSuccess(String response);
        void onError(String error);
        void onThinking();
    }

    public static class Builder {
        private final Context context;
        private String userApiKey;
        private String modelName = "gemini-1.5-flash";
        private int maxTokens = 2500;
        private double temperature = 1.0;
        private boolean showThinking = false;
        private String thinkingText = "Thinking...";
        private TextView responseTextView;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder userApiKey(String apiKey) {
            this.userApiKey = apiKey;
            return this;
        }

        public Builder model(String model) {
            this.modelName = model;
            return this;
        }

        public Builder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder showThinking(boolean showThinking) {
            this.showThinking = showThinking;
            return this;
        }

        public Builder thinkingText(String thinkingText) {
            this.thinkingText = thinkingText;
            return this;
        }

        public Builder responseTextView(TextView textView) {
            this.responseTextView = textView;
            return this;
        }

        public Gemini build() {
            return new Gemini(this);
        }

        private List<String> loadApiKeysFromRaw(Context context) {
            List<String> keys = new ArrayList<>();
            try {
                InputStream is = context.getResources().openRawResource(R.raw.gemini_api);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JSONArray jsonArray = new JSONArray(sb.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    keys.add(jsonArray.getString(i));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading API keys: " + e.getMessage());
            }
            return keys;
        }
    }

    private Gemini(Builder builder) {
        this.showThinking = builder.showThinking;
        this.thinkingText = builder.thinkingText;
        this.responseTextView = builder.responseTextView;

        String apiKey;
        if (builder.userApiKey != null && !builder.userApiKey.isEmpty()) {
            apiKey = builder.userApiKey;
        } else {
            List<String> apiKeys = builder.loadApiKeysFromRaw(builder.context);
            if (apiKeys.isEmpty()) {
                throw new IllegalStateException("No API keys available.");
            }
            apiKey = apiKeys.get(new Random().nextInt(apiKeys.size()));
        }

        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.setTemperature((float)builder.temperature);
        configBuilder.setMaxOutputTokens(builder.maxTokens);

        com.google.firebase.ai.client.generativeai.type.GenerativeModel baseModel =
                new com.google.firebase.ai.client.generativeai.type.GenerativeModel(
            builder.modelName,
            apiKey,
            configBuilder.build(),
            null, // safety settings
            null // tools
        );

        this.generativeModel = GenerativeModelFutures.from(baseModel);
    }

    public void sendPrompt(String prompt) {
        sendPrompt(prompt, null);
    }

    public void sendPrompt(String prompt, GeminiCallback callback) {
        if (prompt == null || prompt.trim().isEmpty()) {
            handleError("Prompt is empty!", callback);
            return;
        }

        if (showThinking) {
            if (callback != null) {
                callback.onThinking();
            } else if (responseTextView != null) {
                responseTextView.post(() -> responseTextView.setText(thinkingText));
            }
        }

        Content content = new Content.Builder().addText(prompt).build();
        ListenableFuture<GenerateContentResponse> response = generativeModel.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                if (responseTextView != null) {
                    responseTextView.post(() -> responseTextView.setText(resultText));
                }
                if (callback != null) {
                    callback.onSuccess(resultText);
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                String error = "Error: " + t.getMessage();
                Log.e(TAG, error, t);
                handleError(error, callback);
            }
        }, executor);
    }

    private void handleError(String error, GeminiCallback callback) {
        if (callback != null) {
            callback.onError(error);
        } else if (responseTextView != null) {
            responseTextView.post(() -> responseTextView.setText(error));
        }
    }
}