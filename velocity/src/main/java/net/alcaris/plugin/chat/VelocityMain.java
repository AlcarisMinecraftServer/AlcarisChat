package net.alcaris.plugin.chat;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.alcaris.plugin.chat.config.VelocityChatConfig;
import net.alcaris.plugin.chat.language.LanguageManager;
import net.alcaris.plugin.chat.listener.*;
import net.alcaris.plugin.chat.store.PrefixStore;
import net.alcaris.plugin.chat.util.ComponentUtils;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "alcaris_chat",
        name = "AlcarisChat",
        version = "1.0.0",
        description = "Global chat plugin for Alcaris Network Velocity proxy.",
        url = "https://www.alcaris.net",
        authors = {"Alcaris Team"}
)
public final class VelocityMain {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private VelocityChatConfig config;
    private PrefixStore prefixStore;

    @Inject
    public VelocityMain(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        load();

        var eventManager = server.getEventManager();
        eventManager.register(this, new PlayerChatListener(server, config, prefixStore, logger));
        eventManager.register(this, new CommandExecuteListener(server, config));
        eventManager.register(this, new ServerConnectedListener(server, config, this));
        eventManager.register(this, new DisconnectListener(server, config));
        eventManager.register(this, new ProxyPingListener(server, config));
        eventManager.register(this, new PrefixMessageListener(prefixStore, logger));

        server.getChannelRegistrar().register(
                MinecraftChannelIdentifier.from("alcarischat:prefix"));

        logger.info("AlcarisChat (Velocity) has started.");
    }

    private void load() {
        prefixStore = new PrefixStore();

        config = new VelocityChatConfig(dataDirectory, logger);
        config.load();

        LanguageManager languageManager = new LanguageManager(dataDirectory, logger);
        languageManager.loadAndRegister();

        ComponentUtils.resetCache();
    }
}
