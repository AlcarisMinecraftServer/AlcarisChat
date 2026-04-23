package net.alcaris.plugin.chat.listener;

import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Velocityから送信された翻訳済みメッセージを受け取り、
 * DiscordSRV API の processChatMessage() を使ってDiscordへ送信するブリッジ。
 */
public final class DiscordSRVBridge implements PluginMessageListener {

    private final JavaPlugin plugin;
    private final Logger logger;

    public DiscordSRVBridge(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!"alcarischat:discord".equals(channel)) return;

        logger.info("[DiscordSRVBridge] Plugin message received from player: " + player.getName());

        boolean discordSrvEnabled = plugin.getServer().getPluginManager().isPluginEnabled("DiscordSRV");
        logger.info("[DiscordSRVBridge] DiscordSRV enabled: " + discordSrvEnabled);
        if (!discordSrvEnabled) return;

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String translatedMessage = in.readUTF();
            logger.info("[DiscordSRVBridge] Sending to Discord - player=" + player.getName() + ", message=" + translatedMessage);

            // DiscordSRVのChannels設定に定義されている最初のチャンネルキーを使用（通常は "global"）
            String gameChannel = DiscordSRV.getPlugin().getChannels().keySet().stream()
                    .findFirst().orElse("global");
            logger.info("[DiscordSRVBridge] Using game channel: " + gameChannel);
            DiscordSRV.getPlugin().processChatMessage(player, translatedMessage, gameChannel, false);

            logger.info("[DiscordSRVBridge] processChatMessage() called successfully.");
        } catch (IOException e) {
            logger.warning("[DiscordSRVBridge] Failed to read translated message: " + e.getMessage());
        }
    }
}
