package net.alcaris.plugin.chat.listener;

import net.alcaris.plugin.chat.prefix.PrefixProvider;
import net.alcaris.plugin.chat.prefix.PrefixSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    private final PrefixProvider prefixProvider;
    private final PrefixSender prefixSender;

    public PlayerJoinListener(PrefixProvider prefixProvider, PrefixSender prefixSender) {
        this.prefixProvider = prefixProvider;
        this.prefixSender = prefixSender;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        prefixSender.sendPrefix(player, prefixProvider.getPrefix(player));
    }
}
