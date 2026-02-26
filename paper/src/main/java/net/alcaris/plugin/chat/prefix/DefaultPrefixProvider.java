package net.alcaris.plugin.chat.prefix;

import net.alcaris.plugin.chat.config.PaperChatConfig;
import org.bukkit.entity.Player;

public final class DefaultPrefixProvider implements PrefixProvider {

    private final PaperChatConfig config;

    public DefaultPrefixProvider(PaperChatConfig config) {
        this.config = config;
    }

    @Override
    public String getPrefix(Player player) {
        return config.getDefaultPrefix();
    }
}
