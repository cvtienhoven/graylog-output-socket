package org.graylog;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class SocketOutputMetaData implements PluginMetaData {
    @Override
    public String getUniqueId() {
        return "org.graylog.SocketOutputPlugin";
    }

    @Override
    public String getName() {
        return "SocketOutput";
    }

    @Override
    public String getAuthor() {
        // TODO Insert author name
        return "SocketOutput author";
    }

    @Override
    public URI getURL() {
        // TODO Insert correct plugin website
        return URI.create("https://www.graylog.org/");
    }

    @Override
    public Version getVersion() {
        return new Version(1, 0, 0);
    }

    @Override
    public String getDescription() {
        // TODO Insert correct plugin description
        return "Description of SocketOutput plugin";
    }

    @Override
    public Version getRequiredVersion() {
        return new Version(1, 0, 0);
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
