package net.alcaris.plugin.chat.japanize;

import java.util.LinkedHashMap;
import java.util.Map;

public final class YukiKanaConverter {

    private static final String[] ROMAJI_LIST;
    private static final String[] HIRAGANA_LIST;

    private YukiKanaConverter() {}

    static {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("a", "あ");
        map.put("i", "い"); map.put("yi", "い");
        map.put("u", "う"); map.put("wu", "う"); map.put("whu", "う");
        map.put("e", "え");
        map.put("o", "お");

        map.put("wha", "うぁ");
        map.put("whi", "うぃ"); map.put("wi", "うぃ");
        map.put("whe", "うぇ"); map.put("we", "うぇ");
        map.put("who", "うぉ");

        map.put("wyi", "ゐ");
        map.put("wye", "ゑ");

        map.put("la", "ぁ"); map.put("xa", "ぁ");
        map.put("li", "ぃ"); map.put("xi", "ぃ"); map.put("lyi", "ぃ"); map.put("xyi", "ぃ");
        map.put("lu", "ぅ"); map.put("xu", "ぅ");
        map.put("le", "ぇ"); map.put("xe", "ぇ"); map.put("lye", "ぇ"); map.put("xye", "ぇ");
        map.put("lo", "ぉ"); map.put("xo", "ぉ");

        map.put("ye", "いぇ");

        map.put("ka", "か"); map.put("ca", "か");
        map.put("ki", "き");
        map.put("ku", "く"); map.put("cu", "く"); map.put("qu", "く");
        map.put("ke", "け");
        map.put("ko", "こ"); map.put("co", "こ");

        map.put("kya", "きゃ"); map.put("kyi", "きぃ"); map.put("kyu", "きゅ");
        map.put("kye", "きぇ"); map.put("kyo", "きょ");

        map.put("qya", "くゃ"); map.put("qyu", "くゅ"); map.put("qyo", "くょ");

        map.put("qwa", "くぁ"); map.put("qa", "くぁ"); map.put("kwa", "くぁ");
        map.put("qwi", "くぃ"); map.put("qi", "くぃ"); map.put("qyi", "くぃ");
        map.put("qwu", "くぅ");
        map.put("qwe", "くぇ"); map.put("qe", "くぇ"); map.put("qye", "くぇ");
        map.put("qwo", "くぉ"); map.put("qo", "くぉ"); map.put("kwo", "くぉ");

        map.put("ga", "が"); map.put("gi", "ぎ"); map.put("gu", "ぐ");
        map.put("ge", "げ"); map.put("go", "ご");

        map.put("gya", "ぎゃ"); map.put("gyi", "ぎぃ"); map.put("gyu", "ぎゅ");
        map.put("gye", "ぎぇ"); map.put("gyo", "ぎょ");

        map.put("gwa", "ぐぁ"); map.put("gwi", "ぐぃ"); map.put("gwu", "ぐぅ");
        map.put("gwe", "ぐぇ"); map.put("gwo", "ぐぉ");

        map.put("lka", "ヵ"); map.put("xka", "ヵ");
        map.put("lke", "ヶ"); map.put("xke", "ヶ");

        map.put("sa", "さ");
        map.put("si", "し"); map.put("ci", "し"); map.put("shi", "し");
        map.put("su", "す");
        map.put("se", "せ"); map.put("ce", "せ");
        map.put("so", "そ");

        map.put("sya", "しゃ"); map.put("sha", "しゃ");
        map.put("syi", "しぃ");
        map.put("syu", "しゅ"); map.put("shu", "しゅ");
        map.put("sye", "しぇ"); map.put("she", "しぇ");
        map.put("syo", "しょ"); map.put("sho", "しょ");

        map.put("swa", "すぁ"); map.put("swi", "すぃ"); map.put("swu", "すぅ");
        map.put("swe", "すぇ"); map.put("swo", "すぉ");

        map.put("za", "ざ");
        map.put("zi", "じ"); map.put("ji", "じ");
        map.put("zu", "ず"); map.put("ze", "ぜ"); map.put("zo", "ぞ");

        map.put("zya", "じゃ"); map.put("ja", "じゃ"); map.put("jya", "じゃ");
        map.put("zyi", "じぃ"); map.put("jyi", "じぃ");
        map.put("zyu", "じゅ"); map.put("ju", "じゅ"); map.put("jyu", "じゅ");
        map.put("zye", "じぇ"); map.put("je", "じぇ"); map.put("jye", "じぇ");
        map.put("zyo", "じょ"); map.put("jo", "じょ"); map.put("jyo", "じょ");

        map.put("ta", "た");
        map.put("ti", "ち"); map.put("chi", "ち");
        map.put("tu", "つ"); map.put("tsu", "つ");
        map.put("te", "て"); map.put("to", "と");

        map.put("tya", "ちゃ"); map.put("cha", "ちゃ"); map.put("cya", "ちゃ");
        map.put("tyi", "ちぃ"); map.put("cyi", "ちぃ");
        map.put("tyu", "ちゅ"); map.put("chu", "ちゅ"); map.put("cyu", "ちゅ");
        map.put("tye", "ちぇ"); map.put("che", "ちぇ"); map.put("cye", "ちぇ");
        map.put("tyo", "ちょ"); map.put("cho", "ちょ"); map.put("cyo", "ちょ");

        map.put("tsa", "つぁ"); map.put("tsi", "つぃ");
        map.put("tse", "つぇ"); map.put("tso", "つぉ");

        map.put("tha", "てゃ"); map.put("thi", "てぃ"); map.put("thu", "てゅ");
        map.put("the", "てぇ"); map.put("tho", "てょ");

        map.put("twa", "とぁ"); map.put("twi", "とぃ"); map.put("twu", "とぅ");
        map.put("twe", "とぇ"); map.put("two", "とぉ");

        map.put("da", "だ"); map.put("di", "ぢ"); map.put("du", "づ");
        map.put("de", "で"); map.put("do", "ど");

        map.put("dya", "ぢゃ"); map.put("dyi", "ぢぃ"); map.put("dyu", "ぢゅ");
        map.put("dye", "ぢぇ"); map.put("dyo", "ぢょ");

        map.put("dha", "でゃ"); map.put("dhi", "でぃ"); map.put("dhu", "でゅ");
        map.put("dhe", "でぇ"); map.put("dho", "でょ");

        map.put("dwa", "どぁ"); map.put("dwi", "どぃ"); map.put("dwu", "どぅ");
        map.put("dwe", "どぇ"); map.put("dwo", "どぉ");

        map.put("ltu", "っ"); map.put("xtu", "っ");
        map.put("ltsu", "っ"); map.put("xtsu", "っ");

        map.put("na", "な"); map.put("ni", "に"); map.put("nu", "ぬ");
        map.put("ne", "ね"); map.put("no", "の");

        map.put("nya", "にゃ"); map.put("nyi", "にぃ"); map.put("nyu", "にゅ");
        map.put("nye", "にぇ"); map.put("nyo", "にょ");

        map.put("ha", "は"); map.put("hi", "ひ");
        map.put("hu", "ふ"); map.put("fu", "ふ");
        map.put("he", "へ"); map.put("ho", "ほ");

        map.put("hya", "ひゃ"); map.put("hyi", "ひぃ"); map.put("hyu", "ひゅ");
        map.put("hye", "ひぇ"); map.put("hyo", "ひょ");

        map.put("fwa", "ふぁ"); map.put("fa", "ふぁ");
        map.put("fwi", "ふぃ"); map.put("fi", "ふぃ"); map.put("fyi", "ふぃ");
        map.put("fwu", "ふぅ");
        map.put("fwe", "ふぇ"); map.put("fe", "ふぇ"); map.put("fye", "ふぇ");
        map.put("fwo", "ふぉ"); map.put("fo", "ふぉ");

        map.put("fya", "ふゃ"); map.put("fyu", "ふゅ"); map.put("fyo", "ふょ");

        map.put("ba", "ば"); map.put("bi", "び"); map.put("bu", "ぶ");
        map.put("be", "べ"); map.put("bo", "ぼ");

        map.put("bya", "びゃ"); map.put("byi", "びぃ"); map.put("byu", "びゅ");
        map.put("bye", "びぇ"); map.put("byo", "びょ");

        map.put("va", "ヴぁ"); map.put("vi", "ヴぃ"); map.put("vu", "ヴ");
        map.put("ve", "ヴぇ"); map.put("vo", "ヴぉ");

        map.put("vya", "ヴゃ"); map.put("vyi", "ヴぃ"); map.put("vyu", "ヴゅ");
        map.put("vye", "ヴぇ"); map.put("vyo", "ヴょ");

        map.put("pa", "ぱ"); map.put("pi", "ぴ"); map.put("pu", "ぷ");
        map.put("pe", "ぺ"); map.put("po", "ぽ");

        map.put("pya", "ぴゃ"); map.put("pyi", "ぴぃ"); map.put("pyu", "ぴゅ");
        map.put("pye", "ぴぇ"); map.put("pyo", "ぴょ");

        map.put("ma", "ま"); map.put("mi", "み"); map.put("mu", "む");
        map.put("me", "め"); map.put("mo", "も");

        map.put("mya", "みゃ"); map.put("myi", "みぃ"); map.put("myu", "みゅ");
        map.put("mye", "みぇ"); map.put("myo", "みょ");

        map.put("ya", "や"); map.put("yu", "ゆ"); map.put("yo", "よ");

        map.put("lya", "ゃ"); map.put("xya", "ゃ");
        map.put("lyu", "ゅ"); map.put("xyu", "ゅ");
        map.put("lyo", "ょ"); map.put("xyo", "ょ");

        map.put("ra", "ら"); map.put("ri", "り"); map.put("ru", "る");
        map.put("re", "れ"); map.put("ro", "ろ");

        map.put("rya", "りゃ"); map.put("ryi", "りぃ"); map.put("ryu", "りゅ");
        map.put("rye", "りぇ"); map.put("ryo", "りょ");

        map.put("wa", "わ"); map.put("wo", "を");
        map.put("lwa", "ゎ"); map.put("xwa", "ゎ");

        map.put("nn", "ん"); map.put("n'", "ん"); map.put("xn", "ん"); map.put("n", "ん");

        Map<String, String> snapshot = new LinkedHashMap<>(map);
        for (Map.Entry<String, String> entry : snapshot.entrySet()) {
            String romaji = entry.getKey();
            if (canStartFromSokuon(romaji)) {
                String doubled = romaji.charAt(0) + romaji;
                if (!map.containsKey(doubled)) map.put(doubled, "っ" + entry.getValue());
            }
        }

        map.put("-", "ー");
        map.put(",", "、");
        map.put(".", "。");
        map.put("?", "？");
        map.put("!", "！");
        map.put("[", "「"); map.put("]", "」");
        map.put("<", "＜"); map.put(">", "＞");
        map.put("&", "＆");
        map.put("\"", "“");
        map.put("(", "（"); map.put(")", "）");

        String[] keys = map.keySet().toArray(new String[0]);
        java.util.Arrays.sort(keys, (a, b) -> b.length() - a.length());

        ROMAJI_LIST = keys;
        HIRAGANA_LIST = new String[keys.length];
        for (int i = 0; i < keys.length; i++) HIRAGANA_LIST[i] = map.get(keys[i]);
    }

    private static boolean canStartFromSokuon(String romaji) {
        if (romaji.isEmpty()) return false;
        char c = romaji.charAt(0);
        return c != 'a' && c != 'i' && c != 'u' && c != 'e' && c != 'o' && c != 'n';
    }

    public static String conv(String romaji) {
        if (romaji == null || romaji.isEmpty()) return romaji;
        StringBuilder result = new StringBuilder();
        int pos = 0;
        outer:
        while (pos < romaji.length()) {
            for (int i = 0; i < ROMAJI_LIST.length; i++) {
                if (romaji.startsWith(ROMAJI_LIST[i], pos)) {
                    result.append(HIRAGANA_LIST[i]);
                    pos += ROMAJI_LIST[i].length();
                    continue outer;
                }
            }
            result.append(romaji.charAt(pos++));
        }
        return result.toString();
    }

}
