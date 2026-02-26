package net.alcaris.plugin.template;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

//Please ensure that the ID used here is the same as alcaris{plugin_id}.
//The plugin_id should be the same as the one specified in build.gradle.
@Plugin(id = "alcaristemplate")
public final class VelocityMain {

    private final ProxyServer server;

    @Inject
    public VelocityMain(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        server.getConsoleCommandSource().sendMessage(
                net.kyori.adventure.text.Component.text("Velocity plugin enabled!")
        );
        SharedUtil.printStartupMessage("Velocity");
    }
}