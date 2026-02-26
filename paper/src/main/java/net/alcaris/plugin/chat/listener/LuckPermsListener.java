package net.alcaris.plugin.chat.listener;

import net.alcaris.plugin.chat.prefix.PrefixProvider;
import net.alcaris.plugin.chat.prefix.PrefixSender;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class LuckPermsListener {

    private final PrefixProvider prefixProvider;
    private final PrefixSender prefixSender;

    public LuckPermsListener(PrefixProvider prefixProvider, PrefixSender prefixSender) {
        this.prefixProvider = prefixProvider;
        this.prefixSender = prefixSender;
    }

    public void onUserDataRecalculate(UserDataRecalculateEvent event) {
        Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
        if (player != null && player.isOnline()) {
            prefixSender.sendPrefix(player, prefixProvider.getPrefix(player));
        }
    }
}
