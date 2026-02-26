package net.alcaris.plugin.chat.util;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public final class TabListUtils {

    private TabListUtils() {}

    public static void refresh(@NotNull ProxyServer proxyServer) {
        Collection<Player> allPlayers = proxyServer.getAllPlayers();
        for (Player player1 : allPlayers) {
            TabList tabList = player1.getTabList();
            RegisteredServer server1 = player1.getCurrentServer()
                    .map(ServerConnection::getServer).orElse(null);

            for (Player player2 : allPlayers) {
                if (player1.equals(player2)) continue;
                Optional<ServerConnection> optional2 = player2.getCurrentServer();
                if (optional2.isPresent() && optional2.get().getServer().equals(server1)) continue;

                tabList.addEntry(
                        tabList.removeEntry(player2.getUniqueId())
                                .orElse(TabListEntry.builder()
                                        .tabList(tabList)
                                        .profile(player2.getGameProfile())
                                        .build())
                                .setDisplayName(Component.text(player2.getUsername())
                                        .style(Style.style(
                                                TextColor.color(0x6D8BBF),
                                                TextDecoration.UNDERLINED,
                                                TextDecoration.ITALIC)))
                );
            }
        }
    }

    public static void remove(@NotNull Player player, @NotNull ProxyServer proxyServer) {
        UUID uniqueId = player.getUniqueId();
        proxyServer.getAllPlayers().forEach(p -> p.getTabList().removeEntry(uniqueId));
    }
}
