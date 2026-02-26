package net.alcaris.plugin.chat.japanize;

public enum JapanizeType {

    NONE("none"),
    KANA("kana"),
    GOOGLE_IME("googleime");

    private final String id;

    JapanizeType(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    public static JapanizeType fromId(String id, JapanizeType def) {
        if (id == null) return def;
        for (JapanizeType type : values()) {
            if (type.id.equalsIgnoreCase(id)) return type;
        }
        return def;
    }
}
