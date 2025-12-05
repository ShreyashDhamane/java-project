package com.client.api;

import org.json.JSONObject;

import com.client.constants.Constants;
import com.client.core.AppState;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthAPI {

    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json");

    // LOGIN,  receives JWT and stores in AppState
    public static boolean login(
        String username,
        String password
        ) throws Exception {

        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        Request req = new Request.Builder()
            .url(Constants.BASE_URL + "/auth/login")
            .post(RequestBody.create(json, JSON))
            .build();

        try (Response r = httpClient.newCall(req).execute()) {

            if (r.body() == null) return false;

            String body = r.body().string();

            JSONObject obj = new JSONObject(body);

            if (!obj.getBoolean("success")) {
                return false;
            }

            // Store JWT token in global AppState
            String token = obj.getString("token");
            AppState.getInstance().setJwtToken(token);

            // store username
            AppState.getInstance().setUsername(username);

            return true;
        }
    }

    // REGISTER, should return OK/ERROR from backend
    public static boolean register(
        String username,
        String email,
        String password
        ) throws Exception {

        String json = String.format(
            "{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}",
            username, email, password
        );

        Request req = new Request.Builder()
            .url(Constants.BASE_URL + "/auth/register")
            .post(RequestBody.create(json, JSON))
            .build();

        try (Response r = httpClient.newCall(req).execute()) {
            if (r.body() == null) {
                return false;
            }

            String body = r.body().string();
            JSONObject obj = new JSONObject(body);

            return obj.getBoolean("success");
        }
    }
}
