package org.mason.velocityutil.AntiVPN;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JoinListener {
    private final IPsConfiguration plugin;

    public JoinListener(IPsConfiguration plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        plugin.loadAllowedIPs();
        Player player = event.getPlayer();
        String ip = player.getRemoteAddress().getHostString();

        if (plugin.getAllowedIPs().contains(ip)) {
            return;
        }

        if (plugin.getAllowedIPs().contains(event.getPlayer().getUsername())) {
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL("http://proxycheck.io/v2/" + ip + "?vpn=1&asn=1?key=09324e-13188t-qq872p-f80h13"); HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                }
                rd.close();

                // Parse JSON response
                String result = response.toString();
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();

                JsonObject ipInfo = jsonObject.getAsJsonObject(ip);
                if (ipInfo == null) {
                    System.out.println("No data for this IP: " + ip);
                    return;
                }

                String proxyStatus = ipInfo.has("proxy") ? ipInfo.get("proxy").getAsString() : null;
                String proxyType = ipInfo.has("type") ? ipInfo.get("type").getAsString() : null;
                String asn = ipInfo.has("asn") ? ipInfo.get("asn").getAsString() : null;
                String provider = ipInfo.has("provider") ? ipInfo.get("provider").getAsString() : null;
                String country = ipInfo.has("country") ? ipInfo.get("country").getAsString() : null;
                String region = ipInfo.has("region") ? ipInfo.get("region").getAsString() : null;
                String city = ipInfo.has("city") ? ipInfo.get("city").getAsString() : null;

                System.out.println("Proxy Status: " + (proxyStatus != null && proxyStatus.equals("yes") ? "Yes, type: " + proxyType : "No"));
                System.out.println("ASN: " + asn);
                System.out.println("Provider: " + provider);
                System.out.println("Country: " + country);
                System.out.println("Region: " + region);
                System.out.println("City: " + city);

                if (player != null && proxyStatus != null && proxyStatus.equals("yes")) {
                    Component disconnectMessage = Component.text("VPNs are not allowed. If you're not on a VPN submit a ticket on our discord.gg/akurra");
                    player.disconnect(disconnectMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}