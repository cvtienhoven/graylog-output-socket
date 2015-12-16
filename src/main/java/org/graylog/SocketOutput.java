package org.graylog;

import java.io.IOException;
import java.util.List;

import org.graylog2.plugin.Message;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.inputs.annotations.FactoryClass;
import org.graylog2.plugin.outputs.MessageOutput;
import org.graylog2.plugin.outputs.MessageOutputConfigurationException;
import org.graylog2.plugin.streams.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * This is the plugin. Your class should implement one of the existing plugin
 * interfaces. (i.e. AlarmCallback, MessageInput, MessageOutput)
 */
public class SocketOutput implements MessageOutput {
    private static final int PORT = 1978;
	private static final Logger LOG = LoggerFactory.getLogger(SocketOutput.class);
	private ConnectionManager connectionManager;
	private boolean running = false;
	private Configuration configuration;
	
	@Inject
    public SocketOutput(@Assisted Stream stream, @Assisted Configuration configuration) throws MessageOutputConfigurationException {
        this.configuration = configuration;

        // Check configuration.
        //if (!checkConfiguration(configuration)) {
        //    throw new MessageOutputConfigurationException("Missing configuration.");
        //}

        // Set up sender.
        LOG.info("Starting SocketOutput");
		connectionManager = new ConnectionManager(PORT);
        connectionManager.start();
 


        running = true;
    }
	
	 @FactoryClass
	 public interface Factory extends MessageOutput.Factory<SocketOutput> {
	        @Override
	        SocketOutput create(Stream stream, Configuration configuration);

	        @Override
	        Config getConfig();

	        @Override
	        Descriptor getDescriptor();
	}
	 public static class Descriptor extends MessageOutput.Descriptor
	    {
	        public Descriptor()
	        {
	            super((new SocketOutputMetaData()).getName(), false, "", (new SocketOutputMetaData()).getDescription());
	        }
	    }
	
	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return running;
	}

	@Override
	public void stop() {
		try {
			connectionManager.halt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		running = false;
		
	}

	@Override
	public void write(Message message) throws Exception {
		connectionManager.publish(buildTailMessage(message));
	}

	@Override
	public void write(List<Message> messages) throws Exception {
		LOG.debug("SocketOutput Messages Size " + messages.size());
        for (Message message : messages) {
			connectionManager.publish(buildTailMessage(message));
        }
		
	}
	
	private String buildTailMessage(Message msg) {
		StringBuilder sb = new StringBuilder();
		//msg.getTimestamp();
		sb.append("[source=").append(msg.getSource()).append("] ");
		sb.append("[timestamp=").append(msg.getTimestamp()).append("] ");
		sb.append(msg.getMessage());
		return sb.toString();
	}
}
