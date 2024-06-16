package com.ropulva.calendar.business.layer.common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class Utility {

    public static boolean isNetworkAvailable() {
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setConnectTimeout(1000);
            urlConnect.getContent();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
