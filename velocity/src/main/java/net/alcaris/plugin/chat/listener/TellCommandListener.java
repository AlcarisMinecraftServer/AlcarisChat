package net.alcaris.plugin.chat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.alcaris.plugin.chat.config.VelocityChatConfig;
import net.alcaris.plugin.chat.japanize.Japanizer;
import net.alcaris.plugin.chat.japanize.JapanizeType;
import net.alcaris.plugin.chat.store.PrefixStore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.Set;

import static com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult.denied;

public final class TellCommandListener {

    private static final Set<String> DM_VERBS = Set.of("tell", "msg", "w", "whisper");

    private final ProxyServer proxyServer;
    private final VelocityChatConfig config;
    private final PrefixStore prefixStore;
    private final Logger logger;

    public TellCommandListener(ProxyServer proxyServer, VelocityChatConfig config,
                               PrefixStore prefixStore, Logger logger) {
        this.proxyServer = proxyServer;
        this.config = config;
        this.prefixStore = prefixStore;
        this.logger = logger;
    }

    @Subscribe
    @SuppressWarnings("deprecation")
    public void onCommandExecute(@NotNull CommandExecuteEvent event) {
        if (!event.getResult().isAllowed()
                || !(event.getCommandSource() instanceof Player sender)) return;

        String raw = event.getCommand().trim();
        String[] parts = raw.split("\\s+", 3);
        if (parts.length < 3) return;

        String verb = parts[0].toLowerCase();
        if (!DM_VERBS.contains(verb)) return;

        String recipientName = parts[1];
        String rawMessage = parts[2];

        if (rawMessage.isBlank()) return;

        event.setResult(denied());

        Optional<Player> recipientOpt = proxyServer.getPlayer(recipientName);
        if (recipientOpt.isEmpty()) {
            sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
                    "§cPlayer §f" + recipientName + "§c is not online."));
            return;
        }
        Player recipient = recipientOpt.get();

        if (recipient.getUniqueId().equals(sender.getUniqueId())) {
            sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
                    "§cYou cannot send a message to yourself."));
            return;
        }

        if (config.isColorableChat()) {
            rawMessage = rawMessage.replace("&", "§");
        }

        String senderName = sender.getUsername();
        String senderServerId = sender.getCurrentServer()
                .map(sc -> sc.getServerInfo().getName()).orElse("");
        String senderPrefix = prefixStore.get(senderName, senderServerId);
        String coloredSenderName = applyPrefixColor(senderPrefix, senderName);

        String recipientName2 = recipient.getUsername();
        String recipientServerId = recipient.getCurrentServer()
                .map(sc -> sc.getServerInfo().getName()).orElse("");
        String recipientPrefix = prefixStore.get(recipientName2, recipientServerId);
        String coloredRecipientName = applyPrefixColor(recipientPrefix, recipientName2);

        String convertedMessage = "";
        try {
            convertedMessage = Japanizer.japanize(rawMessage, JapanizeType.GOOGLE_IME);
        } catch (Exception e) {
            logger.error("Japanize failed for PM: sender={}, msg={}", senderName, rawMessage, e);
        }
        boolean wasConverted = !convertedMessage.isEmpty() && !convertedMessage.equals(rawMessage);

        String label = "§7[" + senderPrefix + coloredSenderName + "§7 -> " + recipientPrefix + coloredRecipientName + "§7]§f ";
        String msg = label + rawMessage;
        if (wasConverted) msg += " §6(" + convertedMessage + ")";

        Component component = LegacyComponentSerializer.legacySection().deserialize(msg);
        sender.sendMessage(component);
        recipient.sendMessage(component);
    }

    private static String applyPrefixColor(String prefix, String playerName) {
        if (prefix.isEmpty()) return playerName;
        String lastColor = "";
        for (int i = prefix.length() - 2; i >= 0; i--) {
            if (prefix.charAt(i) == '§' && i + 1 < prefix.length()) {
                char c = prefix.charAt(i + 1);
                if (isValidColorChar(c)) {
                    lastColor = "§" + c;
                    break;
                }
            }
        }
        return lastColor + playerName;
    }

    private static boolean isValidColorChar(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')
                || c == 'k' || c == 'l' || c == 'm' || c == 'n' || c == 'o' || c == 'r';
    }
}
