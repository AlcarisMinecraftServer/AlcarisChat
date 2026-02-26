package net.alcaris.plugin.chat.japanize;

public final class Japanizer {

    private static final String REGEX_URL = "https?://[\\w/:%#$&?()~.=+\\-]+";

    private Japanizer() {}

    public static String japanize(String org, JapanizeType type) {
        if (type == JapanizeType.NONE || !isNeedToJapanize(org)) return "";

        String text = org.replaceAll(REGEX_URL, " ");
        String japanized = YukiKanaConverter.conv(text);
        if (type == JapanizeType.GOOGLE_IME) japanized = IMEConverter.convByGoogleIME(japanized);
        return japanized.trim();
    }

    private static boolean isNeedToJapanize(String org) {
        return (org.getBytes().length == org.length() && !org.matches("[ ｡-ﾟ]+"));
    }
}
