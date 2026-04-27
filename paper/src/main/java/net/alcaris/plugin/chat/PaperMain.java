package net.alcaris.plugin.chat;

import net.alcaris.plugin.chat.config.PaperChatConfig;
import net.alcaris.plugin.chat.listener.*;
import net.alcaris.plugin.chat.prefix.*;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperMain extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperChatConfig config = new PaperChatConfig(this);
        config.load();

        PrefixProvider prefixProvider;
        LuckPerms luckPermsApi = null;

        if (config.isUseLuckperms() && getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            try {
                luckPermsApi = LuckPermsProvider.get();
                prefixProvider = new LuckPermsPrefixProvider(luckPermsApi);
                getLogger().info("LuckPerms integration enabled.");
            } catch (IllegalStateException e) {
                getLogger().warning("LuckPerms API unavailable, using default prefix provider.");
                prefixProvider = new DefaultPrefixProvider(config);
            }
        } else {
            prefixProvider = new DefaultPrefixProvider(config);
            if (config.isUseLuckperms()) getLogger().warning("LuckPerms not found, using default prefix provider.");
        }

        PrefixSender prefixSender = new PrefixSender(this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "alcarischat:prefix");
        getServer().getMessenger().registerIncomingPluginChannel(this, "alcarischat:discord", new DiscordSRVBridge(this));

        var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(prefixProvider, prefixSender), this);
        pluginManager.registerEvents(new PlayerQuitListener(prefixSender), this);
        pluginManager.registerEvents(new PlayerChatListener(), this);

        if (luckPermsApi != null) {
            LuckPermsListener luckPermsListener = new LuckPermsListener(prefixProvider, prefixSender);
            luckPermsApi.getEventBus().subscribe(this, UserDataRecalculateEvent.class,
                    luckPermsListener::onUserDataRecalculate);
        }

        getLogger().info("AlcarisChat (Paper) started.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AlcarisChat (Paper) stopped.");
    }
}
