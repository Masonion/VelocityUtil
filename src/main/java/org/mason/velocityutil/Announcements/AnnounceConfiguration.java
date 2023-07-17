package org.mason.velocityutil.Announcements;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AnnounceConfiguration {

    private List<String> messages;
    private static PluginContainer container;

    // Constructor
    public AnnounceConfiguration(PluginContainer container) {
        this.container = container;
        this.messages = new ArrayList<>();  // initialization
    }

    // Getter method
    public List<String> getMessages() {
        return this.messages;
    }

    // Setter method
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void loadMessages() {
        Yaml yaml = new Yaml();
        try {
            PluginDescription description = container.getDescription();
            Path pluginPath = description.getSource().get();
            Path dataFolderPath = pluginPath.getParent().resolve(description.getId());

            FileInputStream fis = new FileInputStream(new File(dataFolderPath.toFile(), "announcements.yml"));
            List<String> loadedMessages = yaml.load(fis);

            messages.clear();
            messages.addAll(loadedMessages);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}