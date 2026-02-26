package net.alcaris.plugin.chat.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperChatConfig {

    private final JavaPlugin plugin;

    private boolean useLuckperms = true;
    private String defaultPrefix = "";

    public PaperChatConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        this.useLuckperms = config.getBoolean("use_luckperms", true);
        this.defaultPrefix = config.getString("default_prefix", "");
    }

    public boolean isUseLuckperms() { return useLuckperms; }
    public String getDefaultPrefix() { return defaultPrefix; }
}
