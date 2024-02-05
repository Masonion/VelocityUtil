package org.mason.velocityutil.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

public class StaffChatCommand implements SimpleCommand {

    private final ProxyServer server;
    private final Logger logger;
    private final LuckPerms luckPerms;

    public StaffChatCommand(ProxyServer server, Logger logger, LuckPerms luckPerms) {
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
        if (args.length > 0) {
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            if (user != null && user.getCachedData().getPermissionData().checkPermission("akurra.mod").asBoolean()) {
                String message = String.join(" ", args);
                Component chatMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(
                        "&9" + "[SC]" + "&7" + "[" + player.getCurrentServer().get().getServerInfo().getName().toUpperCase() + "]" +
                                user.getCachedData().getMetaData().getPrefix() + player.getUsername() + "&r" + ": " + message);
                server.getAllPlayers().stream().filter(staff -> staff.hasPermission("akurra.mod")).forEach(staff -> staff.sendMessage(chatMessage));
            } else {
                player.sendMessage(Component.text("You don't have permission to use this command."));
            }
        } else {
            player.sendMessage(Component.text("Please enter a message to send to staff chat."));
        }
    }
}