package net.alcaris.plugin.chat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.alcaris.plugin.chat.config.VelocityChatConfig;
import net.alcaris.plugin.chat.japanize.Japanizer;
import net.alcaris.plugin.chat.japanize.JapanizeType;
import net.alcaris.plugin.chat.store.PrefixStore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult.denied;

public final class PlayerChatListener {

    private final ProxyServer proxyServer;
    private final VelocityChatConfig config;
    private final PrefixStore prefixStore;
    private final Logger logger;

    private final Map<UUID, AtomicBoolean> processingPlayers = new ConcurrentHashMap<>();

    private static String applyPrefixColor(String prefix, String playerName) {
        if (prefix.isEmpty()) return playerName;
        String lastColor = "";
        for (int i = prefix.length() - 2; i >= 0; i--) {
            if (prefix.charAt(i) == '§' && i + 1 < prefix.length()) {
                char c = prefix.charAt(i + 1);
                if (isValidColorChar(c)) {
                    lastColor = "§" + c;
                    break;
                }
            }
        }
        return lastColor + playerName;
    }

    public PlayerChatListener(ProxyServer proxyServer, VelocityChatConfig config,
                              PrefixStore prefixStore, Logger logger) {
        this.proxyServer = proxyServer;
        this.config = config;
        this.prefixStore = prefixStore;
        this.logger = logger;
    }

    @Subscribe
    @SuppressWarnings("deprecation")
    public void onPlayerChat(@NotNull PlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        AtomicBoolean processing = processingPlayers.computeIfAbsent(playerId, k -> new AtomicBoolean(false));

        if (!processing.compareAndSet(false, true)) {
            event.setResult(denied());
            return;
        }

        try {
            event.setResult(denied());
            String playerMessage = event.getMessage();
            if (playerMessage == null || playerMessage.isBlank()) return;

            if (config.isColorableChat()) {
                playerMessage = playerMessage.replace("&", "§");
            }

            String playerName = player.getUsername();
            Optional<ServerConnection> currentServerOpt = player.getCurrentServer();
            RegisteredServer currentServer = currentServerOpt.map(ServerConnection::getServer).orElse(null);
            String serverId = currentServerOpt.map(sc -> sc.getServerInfo().getName()).orElse("");

            String convertedMessage = "";
            try {
                convertedMessage = Japanizer.japanize(playerMessage, JapanizeType.GOOGLE_IME);
            } catch (Exception e) {
                logger.error("Japanize failed: player={}, msg={}", playerName, playerMessage, e);
            }

            boolean wasConverted = !convertedMessage.isEmpty();
            String prefix = prefixStore.get(playerName, serverId);
            String coloredPlayerName = applyPrefixColor(prefix, playerName);

            Map<String, String> serverNames = config.getServerNames();
            String displayServerName = serverNames.getOrDefault(serverId, serverId);

            final String finalMessage = playerMessage;
            final String finalConverted = convertedMessage;

            if (wasConverted) {
                String sameServerMsg = prefix + coloredPlayerName + "§a: §f" + finalMessage
                        + " §6(" + finalConverted + ")";
                String otherServerMsg = "<white>[</white><dark_green>@" + escape(displayServerName) + "</dark_green><white>]</white> "
                        + "<white>" + escape(playerName) + "</white><green>: </green><white>" + escape(finalMessage)
                        + "</white> <gold>(" + finalConverted + ")</gold>";

                proxyServer.getAllServers().forEach(server -> {
                    if (server.equals(currentServer)) {
                        Component msg = LegacyComponentSerializer.legacySection().deserialize(sameServerMsg);
                        server.getPlayersConnected().forEach(p -> p.sendMessage(msg));
                    } else {
                        server.sendMessage(MiniMessage.miniMessage().deserialize(otherServerMsg));
                    }
                });
                logger.info("[{}]<{}> {} ({})", displayServerName, playerName, finalMessage, finalConverted);
            } else {
                String sameServerMsg = prefix + coloredPlayerName + "§a: §f" + finalMessage;

                if (currentServer != null) {
                    Component msg = LegacyComponentSerializer.legacySection().deserialize(sameServerMsg);
                    currentServer.getPlayersConnected().forEach(p -> p.sendMessage(msg));
                }

                String otherServerMsg = "<white>[</white><dark_green>@" + escape(displayServerName) + "</dark_green><white>]</white> "
                        + "<white>" + escape(playerName) + "</white><green>: </green><white>" + escape(finalMessage) + "</white>";

                proxyServer.getAllServers().forEach(server -> {
                    if (!server.equals(currentServer)) {
                        server.sendMessage(MiniMessage.miniMessage().deserialize(otherServerMsg));
                    }
                });
                logger.info("[{}]<{}> {}", displayServerName, playerName, finalMessage);
            }
        } finally {
            processing.set(false);
        }
    }

    private static boolean isValidColorChar(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')
                || c == 'k' || c == 'l' || c == 'm' || c == 'n' || c == 'o' || c == 'r';
    }

    private static String escape(String s) {
        return s.replace("<", "\\<").replace(">", "\\>");
    }
}
