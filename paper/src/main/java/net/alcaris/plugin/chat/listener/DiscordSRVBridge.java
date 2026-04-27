package net.alcaris.plugin.chat.listener;

import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;

public final class DiscordSRVBridge implements PluginMessageListener {

    private final JavaPlugin plugin;
    private final Logger logger;

    public DiscordSRVBridge(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!"alcarischat:discord".equals(channel)) return;

        boolean discordSrvEnabled = plugin.getServer().getPluginManager().isPluginEnabled("DiscordSRV");

        if (!discordSrvEnabled) return;

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String translatedMessage = in.readUTF();
            String gameChannel = DiscordSRV.getPlugin().getChannels().keySet().stream().findFirst().orElse("global");
            DiscordSRV.getPlugin().processChatMessage(player, translatedMessage, gameChannel, false, null);
        } catch (IOException e) {
            logger.warning("[DiscordSRVBridge] Failed to read translated message: " + e.getMessage());
        }
    }
}
