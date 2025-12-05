package com.client;

import java.util.List;

import com.client.constants.Constants;
import com.client.core.AppState;
import com.client.core.BaseFrame;
import com.client.core.ScreenManager;
import com.client.model.Currency;
import com.client.screens.login.LoginScreen;
import com.client.utils.HttpClient;

public class Main {
    public static void main(String[] args) {

        preloadCurrencies(); // currency is same for all users, so preload it using a separate thread

        //  Start UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            BaseFrame frame = new BaseFrame("Money Manager"); // main frame title
            ScreenManager.init(frame);
            ScreenManager.show(new LoginScreen()); // show login screen first, we can add local state management to check if user is logged in or not
            frame.setVisible(true);
        });
    }

    private static void preloadCurrencies() {
        new Thread(() -> {
            try {
                // using client solves lot of code duplication, better than okhttp for simple get requests
                String jsonResponse = HttpClient.get(Constants.BASE_URL + "/currency");
                if (jsonResponse == null || jsonResponse.isEmpty()) {
                    return;
                }
                // parse list of currencies
                List<Currency> list = HttpClient.fromJsonList(jsonResponse, Currency.class);
                if (list != null) {
                    // store it in state management throught the app
                    AppState.getInstance().setCurrencies(list);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start(); // craete and start thread immediately, no need to keep a reference
    }
}
