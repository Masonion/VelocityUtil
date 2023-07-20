package org.mason.velocityutil.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

public class AnnounceCommand implements SimpleCommand {

    private final ProxyServer server;
    private final Logger logger;
    private final LuckPerms luckPerms;

    public AnnounceCommand(ProxyServer server, Logger logger, LuckPerms luckPerms) {
        this.server = server;
        this.logger = logger;
        this.luckPerms = luckPerms;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player)) {
            logger.info("This command can only be run by a player!");
            return;
        }
        Player player = (Player) source;
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null && user.getCachedData().getPermissionData().checkPermission("akurra.admin").asBoolean()) {
            if (args.length > 0) {
                String message = String.join(" ", args);
                Component chatMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(
                        "\n" + "&4" + "[SERVER ANNOUNCEMENT] " + "\n" + "&f" + message + "\n");
                server.getAllServers().forEach(server -> server.getPlayersConnected().forEach(p -> {
                    p.sendMessage(chatMessage);
                }));
            } else {
                player.sendMessage(Component.text("Please enter a message to announce."));
            }
        } else {
            player.sendMessage(Component.text("You don't have permission to use this command."));
        }
    }
}
