package com.shashov.currency.common;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class NetworkCall {
    private static final int READ_TIMEOUT = 3000;
    private static final int CONNECT_TIMEOUT = 3000;

    /**
     * If the network request is successful, it returns the response body in String form.
     * Otherwise, it will throw an IOException.
     */
    public static String downloadUrl(@NonNull URL url) throws IOException {
        InputStream stream = null;
        HttpURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms
            connection.setReadTimeout(READ_TIMEOUT);
            // Timeout for connection.connect() arbitrarily set to 3000ms
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String
                result = readStream(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    private static String readStream(@NonNull InputStream iStream) throws IOException {
        String singleLine;
        StringBuilder totalLines = new StringBuilder(iStream.available());
        BufferedReader reader;

        reader = new BufferedReader(new InputStreamReader(iStream, "windows-1251"));
        while ((singleLine = reader.readLine()) != null) {
            totalLines.append(singleLine);
        }

        return totalLines.toString();
    }
}
