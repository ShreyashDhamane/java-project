package com.client.api;

import com.client.constants.Constants;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// auth api for login and register,
public class AuthAPI {  
    // http client instance instead of using our own http client
    // but using own http client saves lot of code duplication
    // i have used both to learn how to use okhttp and also how to use standard java libraries and create own http client
    private static final OkHttpClient httpClient = new OkHttpClient();
    
    public static String login(String email, String password) throws Exception {
        // JSON payload data
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        
        // create request
        Request req = new Request.Builder()
                .url(Constants.BASE_URL + "/auth/login") // url
                .post(RequestBody.create(json, MediaType.get("application/json"))) // post method with json body
                .build(); // build request
        // try with resources to auto close response
        try {
            Response r = httpClient.newCall(req).execute();
            if (r.body() != null) {
                return r.body().string();
            } else {
                return "";
            }
        }catch (Exception e) {
            throw e;
        }
    }

    public static String register(String email, String password) throws Exception {
        // JSON payload data
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        
        // create request
        Request req = new Request.Builder()
                .url(Constants.BASE_URL + "/auth/register")
                .post(RequestBody.create(json, MediaType.get("application/json")))
                .build();

        try {
            Response r = httpClient.newCall(req).execute();
            if (r.body() != null) {
                return r.body().string();
            } else {
                return "";
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
