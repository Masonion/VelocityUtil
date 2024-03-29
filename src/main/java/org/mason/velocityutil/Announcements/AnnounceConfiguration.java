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
import java.util.Map;

public class AnnounceConfiguration {

    private List<String> messages;
    private static PluginContainer container;

    public AnnounceConfiguration(PluginContainer container) {
        this.container = container;
        this.messages = new ArrayList<>();
    }

    public List<String> getMessages() {
        return this.messages;
    }

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
            List<String> loadedMessages = (List<String>) yaml.load(fis);

            messages.clear();
            messages.addAll(loadedMessages);

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}