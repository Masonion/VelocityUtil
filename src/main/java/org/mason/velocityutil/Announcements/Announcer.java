package org.mason.velocityutil.Announcements;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

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
        executorService.scheduleAtFixedRate(this::broadcastNextMessage, 0, 5, TimeUnit.MINUTES);
    }

    private void broadcastNextMessage() {
        List<String> messages = config.getMessages();
        if (!messages.isEmpty()) {
            int index = currentMessageIndex.getAndUpdate(i -> (i + 1) % messages.size());
            String message = messages.get(index);
            server.getAllPlayers().forEach(player -> player.sendMessage(Component.text(message)));
        }
    }
}