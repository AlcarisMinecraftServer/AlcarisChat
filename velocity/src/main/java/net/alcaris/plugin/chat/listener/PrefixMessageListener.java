package net.alcaris.plugin.chat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import net.alcaris.plugin.chat.store.PrefixStore;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public final class PrefixMessageListener {

    private final PrefixStore prefixStore;
    private final Logger logger;

    public PrefixMessageListener(PrefixStore prefixStore, Logger logger) {
        this.prefixStore = prefixStore;
        this.logger = logger;
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!"alcarischat:prefix".equals(event.getIdentifier().getId())) return;
        if (!(event.getSource() instanceof ServerConnection server)) return;

        String serverId = server.getServerInfo().getName();

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
            String action = in.readUTF();
            String playerName = in.readUTF();

            switch (action) {
                case "SET_PREFIX" -> prefixStore.set(playerName, serverId, in.readUTF());
                case "REMOVE_PREFIX" -> prefixStore.remove(playerName, serverId);
                default -> logger.warn("Unknown prefix action: {}", action);
            }
        } catch (IOException e) {
            logger.error("Failed to process prefix message", e);
        }
    }
}
