package net.alcaris.plugin.chat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.alcaris.plugin.chat.config.VelocityChatConfig;
import net.alcaris.plugin.chat.util.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult.denied;

public final class CommandExecuteListener {

    private final ProxyServer proxyServer;
    private final VelocityChatConfig config;

    public CommandExecuteListener(ProxyServer proxyServer, VelocityChatConfig config) {
        this.proxyServer = proxyServer;
        this.config = config;
    }

    @Subscribe
    public void onCommandExecute(@NotNull CommandExecuteEvent event) {
        if (!event.getResult().isAllowed()
                || !(event.getCommandSource() instanceof Player sourcePlayer)) return;

        if (!event.getCommand().trim().equals("server") || !config.isCustomServerCommand()) return;

        event.setResult(denied());

        Optional<RegisteredServer> serverOpt = sourcePlayer.getCurrentServer()
                .map(ServerConnection::getServer);
        String currentServerId = serverOpt.map(s -> s.getServerInfo().getName()).orElse(null);

        if (serverOpt.isPresent()) {
            sourcePlayer.sendMessage(Component.translatable("chat.command.server.current",
                    ComponentUtils.getServerComponent(serverOpt.get(), config.getServerNames(), currentServerId, 0)));
        }

        Collection<RegisteredServer> allServers = proxyServer.getAllServers();
        if (allServers.size() > 50) {
            sourcePlayer.sendMessage(
                    Component.translatable("velocity.command.server-too-many", NamedTextColor.RED));
            return;
        }

        List<Component> serverComponents = allServers.stream()
                .map(s -> ComponentUtils.getServerComponent(s, config.getServerNames(), currentServerId, 0))
                .toList();
        sourcePlayer.sendMessage(Component.translatable("chat.command.server.available",
                Component.join(JoinConfiguration.separator(
                        Component.text(", ", TextColor.color(0xAAAAAA))), serverComponents)));
    }
}
