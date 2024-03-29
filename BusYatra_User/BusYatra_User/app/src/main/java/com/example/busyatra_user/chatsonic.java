package com.example.busyatra_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chatsonic extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagegpt);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);

        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat(question,Message.SENT_BY_ME);

            messageEditText.setText("");
            callAPI(question);

        });
    }
    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response,Message.SENT_BY_BOT);
    }
    void callAPI(String question){

        //okhttp
       addToChat(question,Message.SENT_BY_ME);
        messageList.add(new Message("Typing... ",Message.SENT_BY_BOT));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\n" +
                "  \"enable_google_results\": \"true\",\n" +
                "  \"enable_memory\": false,\n" +
                "  \"input_text\": \"" +  question + "\"\n" +
                "}");
        Request request = new Request.Builder()
                .url("https://api.writesonic.com/v2/business/content/chatsonic?engine=premium")
                .post(requestBody)
                .addHeader("X-API-KEY", "1ab2410f-2bfe-4ef7-a037-fba2a2925fd6")
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

                    // Parse the JSON response
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);

                        // Retrieve the value of the "message" field
                        String message1 = jsonObject.getString("message");

                        // Remove HTML tags and other unnecessary content
                        message1 = message1.replaceAll("<.*?>", "")
                                .replaceAll("\\[\\d+\\]", "")
                                .trim();

                        // Run UI updates on the main thread
                        String finalMessage = message1;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Remove typing indicator
                                messageList.remove(messageList.size() - 1);

                                // Add extracted message to chat
                                addResponse(finalMessage);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle non-successful response
                }
            }

        });
    }}

