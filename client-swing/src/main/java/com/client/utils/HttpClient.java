package com.client.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.client.core.AppState;
import com.client.core.ScreenManager;
import com.client.screens.login.LoginScreen;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // GET Request (auto adds JWT except /auth/*)
    public static String get(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            // 🔥 Add JWT unless this is an auth endpoint
            if (!urlStr.contains("/auth/")) {
                String token = AppState.getInstance().getJwtToken();
                if (token != null && !token.isEmpty()) {
                    con.setRequestProperty("Authorization", "Bearer " + token);
                }
            }

            return read(con);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // POST Request (auto adds JWT except /auth/*)
    public static String post(String urlStr, String json) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // 🔥 Add JWT unless /auth/*
            if (!urlStr.contains("/auth/")) {
                String token = AppState.getInstance().getJwtToken();
                if (token != null && !token.isEmpty()) {
                    con.setRequestProperty("Authorization", "Bearer " + token);
                }
            }

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes());
            }

            return read(con);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // PUT Request (auto adds JWT)
    public static String put(String urlStr, String json) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // 🔥 Always add JWT (PUT is never used for auth)
            String token = AppState.getInstance().getJwtToken();
            if (token != null && !token.isEmpty()) {
                con.setRequestProperty("Authorization", "Bearer " + token);
            }

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes());
            }

            return read(con);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // DELETE Request (auto adds JWT)
    public static String delete(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("DELETE");

            // 🔥 Always add JWT
            String token = AppState.getInstance().getJwtToken();
            if (token != null && !token.isEmpty()) {
                con.setRequestProperty("Authorization", "Bearer " + token);
            }

            int status = con.getResponseCode();
            if (status == 200 || status == 204) return "OK";

            return read(con);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

   // GLOBAL RESPONSE HANDLER (handles 401 logout)
    private static String read(HttpURLConnection con) throws Exception {

        int status = con.getResponseCode();

        // 🔥 Global 401 handling — auto logout
        if (status == 401) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                    null,
                    "Session expired. Please login again.",
                    "Unauthorized",
                    JOptionPane.WARNING_MESSAGE
                );

                AppState.getInstance().reset();
                ScreenManager.show(new LoginScreen());
            });
            return null;
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        br.close();
        return sb.toString();
    }

    // JSON → Object
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JSON → List<Object>
    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
