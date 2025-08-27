package com.service.studioasinc.AI;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
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

public class GeminiSDK {
    
    private static final String TAG = "GeminiSDK";
    
    // Default configuration
    private List<String> apiKeys;
    private String model = "gemini-1.5-flash";
    private String responseType = "text";
    private String tone = "normal";
    private String size = "normal";
    private int maxTokens = 2500;
    private float temperature = 1.0f;
    private boolean showThinking = false;
    private String thinkingText = "Thinking...";
    private String systemInstruction = "Your name is Synapse AI, you are an AI made for Synapse (social media) assistance";
    
    private Context context;
    private TextView responseTextView;
    private Random random;
    private Executor executor;
    
    public interface GeminiCallback {
        void onSuccess(String response);
        void onError(String error);
        void onThinking();
    }
    
    public static class Builder {
        private Context context;
        private List<String> apiKeys;
        private String model;
        private String responseType;
        private String tone;
        private String size;
        private int maxTokens;
        private float temperature;
        private boolean showThinking;
        private String thinkingText;
        private String systemInstruction;
        private TextView responseTextView;
        
        public Builder(Context context) {
            this.context = context;
            // Load API keys from JSON file
            this.apiKeys = loadApiKeysFromRaw(context);
            
            // Default values
            this.model = "gemini-1.5-flash";
            this.responseType = "text";
            this.tone = "normal";
            this.size = "normal";
            this.maxTokens = 2500;
            this.temperature = 1.0f;
            this.showThinking = false;
            this.thinkingText = "Thinking...";
            this.systemInstruction = "Your name is Synapse AI, you are an AI made for Synapse (social media) assistance";
        }
        
        public Builder model(String model) {
            this.model = model;
            return this;
        }
        
        public Builder responseType(String responseType) {
            this.responseType = responseType;
            return this;
        }
        
        public Builder tone(String tone) {
            this.tone = tone;
            return this;
        }
        
        public Builder size(String size) {
            this.size = size;
            return this;
        }
        
        public Builder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }
        
        public Builder temperature(float temperature) {
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
        
        public Builder systemInstruction(String systemInstruction) {
            this.systemInstruction = systemInstruction;
            return this;
        }
        
        public Builder responseTextView(TextView textView) {
            this.responseTextView = textView;
            return this;
        }
        
        public GeminiSDK build() {
            return new GeminiSDK(this);
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
    
    private GeminiSDK(Builder builder) {
        this.apiKeys = builder.apiKeys;
        this.model = builder.model;
        this.responseType = builder.responseType;
        this.tone = builder.tone;
        this.size = builder.size;
        this.maxTokens = builder.maxTokens;
        this.temperature = builder.temperature;
        this.showThinking = builder.showThinking;
        this.thinkingText = builder.thinkingText;
        this.systemInstruction = builder.systemInstruction;
        this.context = builder.context;
        this.responseTextView = builder.responseTextView;
        this.random = new Random();
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    public void sendPrompt(String prompt) {
        sendPrompt(prompt, null);
    }
    
    public void sendPrompt(String prompt, GeminiCallback callback) {
        if (prompt == null || prompt.trim().isEmpty()) {
            handleError("Prompt is empty!", callback);
            return;
        }
        
        if (apiKeys == null || apiKeys.isEmpty()) {
            handleError("No API keys available!", callback);
            return;
        }
        
        if (showThinking) {
            if (callback != null) {
                callback.onThinking();
            } else if (responseTextView != null) {
                responseTextView.post(() -> responseTextView.setText(thinkingText));
            }
        }
        
        // Randomly select an API key
        String selectedApiKey = apiKeys.get(random.nextInt(apiKeys.size()));
        
        try {
            // Build full system instruction
            String fullSystemInstruction = buildFullSystemInstruction();
            
            // Create the generative model
            GenerativeModel gm = new GenerativeModel(
                    /* modelName */ model,
                    /* apiKey */ selectedApiKey
            );
            
            GenerativeModelFutures modelFutures = GenerativeModelFutures.from(gm);
            
            // Combine system instruction with prompt if available
            String finalPrompt = prompt;
            if (fullSystemInstruction != null && !fullSystemInstruction.trim().isEmpty()) {
                finalPrompt = fullSystemInstruction + "\n\n" + prompt;
            }
            
            // Generate content with simple string prompt
            ListenableFuture<GenerateContentResponse> response = modelFutures.generateContent(finalPrompt);
            
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String responseText = result.getText();
                    if (responseText == null || responseText.trim().isEmpty()) {
                        responseText = "No response generated";
                    }
                    
                    String finalResponseText = responseText;
                    if (responseTextView != null) {
                        responseTextView.post(() -> responseTextView.setText(finalResponseText));
                    }
                    
                    if (callback != null) {
                        callback.onSuccess(finalResponseText);
                    }
                }
                
                @Override
                public void onFailure(Throwable t) {
                    String error = "Error: " + t.getMessage();
                    Log.e(TAG, error, t);
                    handleError(error, callback);
                }
            }, executor);
            
        } catch (Exception e) {
            String error = "Error initializing model: " + e.getMessage();
            Log.e(TAG, error, e);
            handleError(error, callback);
        }
    }
    
    private String buildFullSystemInstruction() {
        StringBuilder instruction = new StringBuilder(systemInstruction);
        
        if (!"normal".equals(tone)) {
            instruction.append(" Respond in a ").append(tone).append(" tone.");
        }
        
        if (!"normal".equals(size)) {
            instruction.append(" Make the response ").append(size).append(" in length.");
        }
        
        if (!"text".equals(responseType)) {
            instruction.append(" Format the response as ").append(responseType).append(".");
        }
        
        return instruction.toString();
    }
    
    private void handleError(String error, GeminiCallback callback) {
        if (callback != null) {
            callback.onError(error);
        } else if (responseTextView != null) {
            responseTextView.post(() -> responseTextView.setText(error));
        }
    }
    
    // Getters and setters for all customization options
    public void setModel(String model) { this.model = model; }
    public void setResponseType(String responseType) { this.responseType = responseType; }
    public void setTone(String tone) { this.tone = tone; }
    public void setSize(String size) { this.size = size; }
    public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
    public void setTemperature(float temperature) { this.temperature = temperature; }
    public void setShowThinking(boolean showThinking) { this.showThinking = showThinking; }
    public void setThinkingText(String thinkingText) { this.thinkingText = thinkingText; }
    public void setSystemInstruction(String systemInstruction) { this.systemInstruction = systemInstruction; }
    public void setResponseTextView(TextView responseTextView) { this.responseTextView = responseTextView; }
    
    public String getModel() { return model; }
    public String getResponseType() { return responseType; }
    public String getTone() { return tone; }
    public String getSize() { return size; }
    public int getMaxTokens() { return maxTokens; }
    public float getTemperature() { return temperature; }
    public boolean isShowThinking() { return showThinking; }
    public String getThinkingText() { return thinkingText; }
    public String getSystemInstruction() { return systemInstruction; }
}