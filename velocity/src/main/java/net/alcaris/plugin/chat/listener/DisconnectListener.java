package net.alcaris.plugin.chat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.alcaris.plugin.chat.config.VelocityChatConfig;
import net.alcaris.plugin.chat.util.ComponentUtils;
import net.alcaris.plugin.chat.util.TabListUtils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class DisconnectListener {

    private final ProxyServer proxyServer;
    private final VelocityChatConfig config;

    public DisconnectListener(ProxyServer proxyServer, VelocityChatConfig config) {
        this.proxyServer = proxyServer;
        this.config = config;
    }

    @Subscribe
    public void onPlayerDisconnect(@NotNull DisconnectEvent event) {
        if (event.getLoginStatus() != DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN) return;

        Player player = event.getPlayer();
        Component playerComp = ComponentUtils.getPlayerComponent(player);
        RegisteredServer server = player.getCurrentServer()
                .map(ServerConnection::getServer).orElse(null);
        Component serverComp = ComponentUtils.getServerComponent(server, config.getServerNames(), null, -1);

        Component msg = Component.translatable("chat.message.disconnect", playerComp, serverComp);
        proxyServer.getAllPlayers().forEach(p -> p.sendMessage(msg));

        ComponentUtils.removeFromCache(player);

        if (config.isShowGlobalTabList()) {
            TabListUtils.remove(player, proxyServer);
        }
    }
}
