package net.alcaris.plugin.chat.prefix;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class PrefixSender {

    private static final String CHANNEL = "alcarischat:prefix";

    private final JavaPlugin plugin;

    public PrefixSender(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendPrefix(Player player, String prefix) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("SET_PREFIX");
            out.writeUTF(player.getName());
            out.writeUTF(prefix);
            player.sendPluginMessage(plugin, CHANNEL, b.toByteArray());
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to send SET_PREFIX: " + e.getMessage());
        }
    }

    public void removePrefix(Player player) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("REMOVE_PREFIX");
            out.writeUTF(player.getName());
            player.sendPluginMessage(plugin, CHANNEL, b.toByteArray());
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to send REMOVE_PREFIX: " + e.getMessage());
        }
    }
}
