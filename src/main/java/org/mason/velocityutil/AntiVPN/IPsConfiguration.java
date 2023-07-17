package org.mason.velocityutil.AntiVPN;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IPsConfiguration {

    private Set<String> allowedIPs;
    private static PluginContainer container;

    // Adjust the constructor to accept PluginContainer
    public IPsConfiguration(PluginContainer container) {
        this.container = container;
        this.allowedIPs = new HashSet<>();  // initialization
    }

    // Getter method
    public Set<String> getAllowedIPs() {
        return this.allowedIPs;
    }

    // Setter method
    public void setAllowedIPs(Set<String> allowedIPs) {
        this.allowedIPs = allowedIPs;
    }

    public void loadAllowedIPs() {
        Yaml yaml = new Yaml();
        try {
            PluginDescription description = container.getDescription();
            Path pluginPath = description.getSource().get();
            Path dataFolderPath = pluginPath.getParent().resolve(description.getId());

            FileInputStream fis = new FileInputStream(new File(dataFolderPath.toFile(), "allowed.yml"));
            List<String> loadedIPs = yaml.load(fis);

            allowedIPs.clear();
            allowedIPs.addAll(loadedIPs);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
