package net.alcaris.plugin.chat.config;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class VelocityChatConfig {

    private final Path dataDirectory;
    private final Logger logger;

    private boolean sendPlayersOnPing = false;
    private boolean showGlobalTabList = false;
    private boolean customServerCommand = true;
    private boolean colorableChat = true;
    private Map<String, String> serverNames = defaultServerNames();

    public VelocityChatConfig(Path dataDirectory, Logger logger) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    public void load() {
        Path configPath = dataDirectory.resolve("config.yml");
        try {
            Files.createDirectories(configPath.getParent());
            if (!Files.exists(configPath)) {
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.yml")) {
                    if (is != null) Files.copy(is, configPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to extract config.yml", e);
        }

        if (!Files.exists(configPath)) {
            logger.warn("config.yml not found, using defaults");
            return;
        }

        Yaml yaml = new Yaml();
        try (Reader reader = Files.newBufferedReader(configPath)) {
            Map<String, Object> data = yaml.load(reader);
            if (data == null) return;

            sendPlayersOnPing = getBool(data, "send_players_on_ping", false);
            showGlobalTabList = getBool(data, "show_global_tab_list", false);
            customServerCommand = getBool(data, "custom_server_command", true);
            colorableChat = getBool(data, "colorable_chat", true);

            Object namesObj = data.get("server_names");
            if (namesObj instanceof Map<?, ?> namesMap) {
                Map<String, String> parsed = new LinkedHashMap<>();
                for (Map.Entry<?, ?> entry : namesMap.entrySet()) {
                    parsed.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
                serverNames = Collections.unmodifiableMap(parsed);
            }
        } catch (IOException e) {
            logger.error("Failed to load config.yml", e);
        }
    }

    public boolean isSendPlayersOnPing() { return sendPlayersOnPing; }
    public boolean isShowGlobalTabList() { return showGlobalTabList; }
    public boolean isCustomServerCommand() { return customServerCommand; }
    public boolean isColorableChat() { return colorableChat; }
    public Map<String, String> getServerNames() { return serverNames; }

    private static boolean getBool(Map<String, Object> data, String key, boolean def) {
        Object val = data.get(key);
        return val instanceof Boolean b ? b : def;
    }

    private static Map<String, String> defaultServerNames() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("s1", "lobby");
        m.put("s2", "sigen");
        m.put("s3", "PvE");
        m.put("s4", "minigame");
        return Collections.unmodifiableMap(m);
    }
}
