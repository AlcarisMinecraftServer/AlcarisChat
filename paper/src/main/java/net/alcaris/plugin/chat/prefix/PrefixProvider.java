package net.alcaris.plugin.chat.prefix;

import org.bukkit.entity.Player;

public interface PrefixProvider {
    String getPrefix(Player player);
}
