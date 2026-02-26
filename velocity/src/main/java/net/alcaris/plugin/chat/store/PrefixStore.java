package net.alcaris.plugin.chat.store;

import java.util.concurrent.ConcurrentHashMap;

public final class PrefixStore {

    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    public void set(String playerName, String serverId, String prefix) {
        store.put(key(serverId, playerName), prefix);
    }

    public String get(String playerName, String serverId) {
        return store.getOrDefault(key(serverId, playerName), "");
    }

    public void remove(String playerName, String serverId) {
        store.remove(key(serverId, playerName));
    }

    private static String key(String serverId, String playerName) {
        return serverId + ":" + playerName;
    }
}
