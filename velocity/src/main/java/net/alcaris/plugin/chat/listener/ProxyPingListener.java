package net.alcaris.plugin.chat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.alcaris.plugin.chat.config.VelocityChatConfig;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class ProxyPingListener {

    private final ProxyServer proxyServer;
    private final VelocityChatConfig config;

    public ProxyPingListener(ProxyServer proxyServer, VelocityChatConfig config) {
        this.proxyServer = proxyServer;
        this.config = config;
    }

    @Subscribe
    public void onProxyPing(@NotNull ProxyPingEvent event) {
        if (!config.isSendPlayersOnPing()) return;

        ServerPing.SamplePlayer[] samplePlayers = proxyServer.getAllPlayers().stream()
                .map(p -> new ServerPing.SamplePlayer(p.getUsername(), p.getUniqueId()))
                .toArray(ServerPing.SamplePlayer[]::new);

        event.setPing(event.getPing().asBuilder()
                .clearSamplePlayers()
                .samplePlayers(samplePlayers)
                .build());
    }
}
