package net.alcaris.plugin.chat.japanize;

import java.util.HashMap;
import java.util.Map;

public final class Japanizer {

    private static final String REGEX_URL = "https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+";

    private Japanizer() {}

    public static String japanize(String org, JapanizeType type, Map<String, String> dictionary) {
        if (type == JapanizeType.NONE || !isNeedToJapanize(org)) return "";

        String deletedURL = org.replaceAll(REGEX_URL, " ");

        HashMap<String, String> keywordMap = new HashMap<>();
        int index = 0;
        String keywordLocked = deletedURL;
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            String dicKey = entry.getKey();
            if (keywordLocked.contains(dicKey)) {
                index++;
                String placeholder = "＜" + makeMultibytesDigit(index) + "＞";
                keywordLocked = keywordLocked.replace(dicKey, placeholder);
                keywordMap.put(placeholder, entry.getValue());
            }
        }

        String japanized = YukiKanaConverter.conv(keywordLocked);
        if (type == JapanizeType.GOOGLE_IME) japanized = IMEConverter.convByGoogleIME(japanized);

        for (Map.Entry<String, String> entry : keywordMap.entrySet()) {
            japanized = japanized.replace(entry.getKey(), entry.getValue());
        }
        return japanized.trim();
    }

    private static String makeMultibytesDigit(int digit) {
        StringBuilder result = new StringBuilder();
        for (char c : Integer.toString(digit).toCharArray()) result.append((char) ('０' + (c - '0')));
        return result.toString();
    }

    private static boolean isNeedToJapanize(String org) {
        return (org.getBytes().length == org.length() && !org.matches("[ \uFF61-\uFF9F]+"));
    }
}
