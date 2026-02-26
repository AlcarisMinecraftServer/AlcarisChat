package net.alcaris.plugin.chat.prefix;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

public final class LuckPermsPrefixProvider implements PrefixProvider {

    private final LuckPerms luckPerms;

    public LuckPermsPrefixProvider(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    @Override
    public String getPrefix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix != null ? prefix : "";
    }
}
