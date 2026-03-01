package net.alcaris.plugin.chat.util;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ComponentUtils {

    private static final Map<Player, Component> PLAYER_COMPONENT_CACHE = new ConcurrentHashMap<>();

    private ComponentUtils() {}

    @NotNull
    public static Component getPlayerComponent(@NotNull Player player) {
        return PLAYER_COMPONENT_CACHE.computeIfAbsent(player, p ->
                Component.text(p.getUsername())
                        .hoverEvent(p.asHoverEvent())
                        .clickEvent(ClickEvent.suggestCommand("/tell " + p.getUsername() + " "))
        );
    }

    public static void removeFromCache(@NotNull Player player) {
        PLAYER_COMPONENT_CACHE.remove(player);
    }

    public static void resetCache() {
        PLAYER_COMPONENT_CACHE.clear();
    }

    @NotNull
    public static Component getServerComponent(
            @Nullable RegisteredServer server,
            @NotNull Map<String, String> serverNames,
            @Nullable String currentServerId,
            int playerCountOffset) {
        if (server == null) return Component.text("<unknown>", NamedTextColor.GRAY);

        String serverId = server.getServerInfo().getName();
        String displayName = serverNames.getOrDefault(serverId, serverId);
        int online = server.getPlayersConnected().size() + playerCountOffset;

        Component nameComponent = Component.text(displayName, NamedTextColor.GREEN);
        Component playerCountText = online == 1
                ? Component.translatable("velocity.command.server-tooltip-player-online", Component.text(online))
                : Component.translatable("velocity.command.server-tooltip-players-online", Component.text(online));

        if (serverId.equals(currentServerId)) {
            return nameComponent.hoverEvent(HoverEvent.showText(
                    Component.translatable("velocity.command.server-tooltip-current-server")
                            .appendNewline().append(playerCountText)
                            .appendNewline().append(Component.text("ID: " + serverId))));
        } else {
            return nameComponent
                    .hoverEvent(HoverEvent.showText(
                            Component.translatable("velocity.command.server-tooltip-offer-connect-server")
                                    .appendNewline().append(playerCountText)
                                    .appendNewline().append(Component.text("ID: " + serverId))));
        }
    }

    @NotNull
    public static Component getServerComponent(
            @Nullable RegisteredServer server,
            @NotNull Map<String, String> serverNames) {
        return getServerComponent(server, serverNames, null, 0);
    }
}
