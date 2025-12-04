package com.client.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClient {

    // object objecetMapper instance is from jackson library to convert JSON to objects and vice versa
    // data from backend APIs to be serialized/deserialized using this
    private static final ObjectMapper objecetMapper = new ObjectMapper();
    
    // get request, standard function
    public static String get(String urlStr) {
        try {
            URL url = new URL(urlStr);
            // open connection to the URL
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            
            return read(httpConnection);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // post request, standard function
    public static String post(String urlStr, String json) {
        try {
            URL url = new URL(urlStr);
            // open connection to the URL
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST"); // set method to POST
            httpConnection.setRequestProperty("Content-Type", "application/json"); // set content type to JSON
            httpConnection.setDoOutput(true); // enable output stream for sending data
            
            try {
                // get output stream to send data
                OutputStream os = httpConnection.getOutputStream();
                os.write(json.getBytes()); // write json data to output stream
                os.flush(); // flush the stream
            }catch (Exception e) {
                e.printStackTrace();
            }

            return read(httpConnection);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // put request, standard function
    public static String put(String urlStr, String json) {
        try {
            URL url = new URL(urlStr);
            // open connection to the URL
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("PUT"); // set method to PUT
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // set content type to JSON
            httpURLConnection.setDoOutput(true); // enable output stream for sending data

            try {
                OutputStream os = httpURLConnection.getOutputStream(); // get output stream to send data
                os.write(json.getBytes()); // write json data to output stream
                os.flush(); // flush the stream
            }catch (Exception e) {
                e.printStackTrace();
            }

            return read(httpURLConnection);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // read response from HttpURLConnection
    private static String read(HttpURLConnection con) throws Exception {
        // read input stream from connection response
        InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
        BufferedReader br = new BufferedReader(inputStreamReader);
        StringBuilder res = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null){
            res.append(line);
        }
        
        br.close();
        return res.toString();
    }

    // function to convert JSON string to object of given class type
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objecetMapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // delete request, standard function
    public static String delete(String urlStr) {
        try {
            URL url = new URL(urlStr);
            // open connection to the URL
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("DELETE"); // set method to DELETE
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // set content type to JSON

            int status = httpURLConnection.getResponseCode();

            // return status OK for 200 and 204 no content, easyr handling on caller side
            if (status == 200 || status == 204) {
                return "OK";
            }

            return read(httpURLConnection); // fallback if server sends a body
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // fromJsonList to convert JSON array string to list of objects of given class type
    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        try {
            return objecetMapper.readValue(
                json,
                objecetMapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAuthorized(String urlStr, String token) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Authorization", "Bearer " + token);

            return read(httpConnection);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String postAuthorized(String urlStr, String json, String token) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + token);
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes());
            }

            return read(con);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String putAuthorized(String urlStr, String json, String token) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + token);
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes());
            }

            return read(con);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String deleteAuthorized(String urlStr, String token) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("DELETE");
            con.setRequestProperty("Authorization", "Bearer " + token);

            int status = con.getResponseCode();
            if (status == 200 || status == 204) return "OK";

            return read(con);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
