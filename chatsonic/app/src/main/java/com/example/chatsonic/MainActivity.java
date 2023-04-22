package com.example.chatsonic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input from EditText
                String inputText = editText.getText().toString();

                // Create request body with input from EditText
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "  \"enable_google_results\": \"true\",\n" +
                        "  \"enable_memory\": false,\n" +
                        "  \"input_text\": \"" + inputText + "\"\n" +
                        "}");

                // Create OkHttpClient instance
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build();

                // Create Request object
                Request request = new Request.Builder()
                        .url("https://api.writesonic.com/v2/business/content/chatsonic?engine=premium")
                        .post(requestBody)
                        .addHeader("X-API-KEY", "c9243437-b28e-4235-93b3-d0577259a43a")
                        .addHeader("accept", "application/json")
                        .addHeader("content-type", "application/json")
                        .build();

                // Enqueue the request asynchronously
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // Handle request failure
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String responseBody = response.body().string();

                            // Run UI updates on the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Update TextView with the response body
                                    textView.setText(responseBody);
                                }
                            });
                        } else {
                            // Handle non-successful response
                        }
                    }
                });
            }
        });
    }
}