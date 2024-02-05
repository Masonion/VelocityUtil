package org.mason.velocityutil.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

public class ServerSwitchCommand implements SimpleCommand {

    private final ProxyServer server;
    private final Logger logger;
    private final String targetServer;

    public ServerSwitchCommand(ProxyServer server, Logger logger, String targetServer) {
        this.server = server;
        this.logger = logger;
        this.targetServer = targetServer;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player)) {
            logger.info("Only players can switch servers!");
            return;
        }
        Player player = (Player) source;

        if (player.getCurrentServer().isPresent() &&
                player.getCurrentServer().get().getServerInfo().getName().equalsIgnoreCase(targetServer)) {
            return;
        }

        if(server.getAllServers().stream().anyMatch(registeredServer -> registeredServer.getServerInfo().getName().equalsIgnoreCase(targetServer))) {
            player.createConnectionRequest(server.getServer(targetServer).get()).fireAndForget();
        } else {
            player.sendMessage(Component.text("No server with that name exists!"));
        }
    }
}