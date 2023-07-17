package org.mason.velocityutil;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.mason.velocityutil.Announcements.AnnounceConfiguration;
import org.mason.velocityutil.Announcements.Announcer;
import org.mason.velocityutil.AntiVPN.IPsConfiguration;
import org.mason.velocityutil.AntiVPN.JoinListener;
import org.slf4j.Logger;

@Plugin(
        id = "velocityutil",
        name = "VelocityUtil",
        version = "1.0-SNAPSHOT"
)
public class VelocityUtil {

    @Inject
    private Logger logger;

    @Inject
    private ProxyServer server;

    @Inject
    private PluginContainer container;

    private IPsConfiguration ipsConfiguration;
    private AnnounceConfiguration announceConfig;
    private Announcer announcer;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        ipsConfiguration = new IPsConfiguration(container); // Initialize instance variable
        this.ipsConfiguration.loadAllowedIPs();
        this.server.getEventManager().register(this, new JoinListener(ipsConfiguration)); // Corrected line

        announceConfig = new AnnounceConfiguration(container);
        announceConfig.loadMessages();

        announcer = new Announcer(announceConfig, server);
        announcer.startBroadcasting();

        logger.info("VelocityUtil is up and running!");
    }
}