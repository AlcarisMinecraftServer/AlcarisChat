package net.alcaris.plugin.chat.listener;

import net.alcaris.plugin.chat.prefix.PrefixSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitListener implements Listener {

    private final PrefixSender prefixSender;

    public PlayerQuitListener(PrefixSender prefixSender) {
        this.prefixSender = prefixSender;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        prefixSender.removePrefix(event.getPlayer());
    }
}
