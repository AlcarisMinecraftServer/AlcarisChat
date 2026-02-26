package net.alcaris.plugin.chat.japanize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class IMEConverter {

    private static final String GOOGLE_IME_URL = "https://www.google.com/transliterate?langpair=ja-Hira|ja&text=";

    private IMEConverter() {}

    public static String convByGoogleIME(String org) {
        if (org.isEmpty()) return "";

        HttpURLConnection conn = null;
        try {
            URL url = new URL(GOOGLE_IME_URL + URLEncoder.encode(org, StandardCharsets.UTF_8));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.connect();

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
            }
            return GoogleIME.parseJson(sb.toString());
        } catch (IOException e) {
            return "";
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
