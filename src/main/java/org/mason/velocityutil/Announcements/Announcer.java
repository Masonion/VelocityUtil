package org.mason.velocityutil.Announcements;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Announcer {

    private final AnnounceConfiguration config;
    private final ProxyServer server;
    private final AtomicInteger currentMessageIndex = new AtomicInteger(0);
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public Announcer(AnnounceConfiguration config, ProxyServer server) {
        this.config = config;
        this.server = server;
    }

    public void startBroadcasting() {
        executorService.scheduleAtFixedRate(this::broadcastNextMessage, 0, 10, TimeUnit.MINUTES);
    }

    private void broadcastNextMessage() {
        List<String> messages = config.getMessages();
        if (!messages.isEmpty()) {
            int index = currentMessageIndex.getAndUpdate(i -> (i + 1) % messages.size());
            String message = messages.get(index);
            Component chatMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
            server.getAllPlayers().forEach(player -> player.sendMessage(chatMessage));
        }
    }
}