package net.alcaris.plugin.chat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.alcaris.plugin.chat.config.VelocityChatConfig;
import net.alcaris.plugin.chat.util.ComponentUtils;
import net.alcaris.plugin.chat.util.TabListUtils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class ServerConnectedListener {

    private final ProxyServer proxyServer;
    private final VelocityChatConfig config;
    private final Object plugin;

    public ServerConnectedListener(ProxyServer proxyServer, VelocityChatConfig config, Object plugin) {
        this.proxyServer = proxyServer;
        this.config = config;
        this.plugin = plugin;
    }

    @Subscribe
    public void onServerConnected(@NotNull ServerConnectedEvent event) {
        Player player = event.getPlayer();
        RegisteredServer targetServer = event.getServer();
        Component playerComp = ComponentUtils.getPlayerComponent(player);
        Component targetServerComp = ComponentUtils.getServerComponent(
                targetServer, config.getServerNames(), null, +1);

        event.getPreviousServer().ifPresentOrElse(
                prevServer -> {
                    Component prevServerComp = ComponentUtils.getServerComponent(
                            prevServer, config.getServerNames());
                    Component msg = Component.translatable(
                            "chat.message.server_switch",
                            playerComp, prevServerComp, targetServerComp);
                    proxyServer.getAllPlayers().forEach(p -> p.sendMessage(msg));
                },
                () -> {
                    Component msg = Component.translatable(
                            "chat.message.connected",
                            playerComp, targetServerComp);
                    proxyServer.getAllPlayers().forEach(p -> p.sendMessage(msg));
                }
        );

        if (config.isShowGlobalTabList()) {
            proxyServer.getScheduler()
                    .buildTask(plugin, () -> TabListUtils.refresh(proxyServer))
                    .delay(2, TimeUnit.SECONDS)
                    .schedule();
        }
    }
}
