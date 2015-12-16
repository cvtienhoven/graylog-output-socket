package org.graylog;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManager extends Thread
{
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);
    private volatile List<EchoThread> echoThreads = new CopyOnWriteArrayList<EchoThread>();

    private int port;

    private ServerSocket serverSocket;
    private boolean halt = false;
    private EchoThread client;
    
    public ConnectionManager(final int port)
    {
        this.port = port;
    }

    public void run()
    {
		LOG.debug("Socket connectionmanager running on port {}", port);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();

        }

        Socket socket;
        while (!this.halt) {

            try {
            	socket = serverSocket.accept();
				LOG.debug("Socket connectionmanager client ... ");
                client = new EchoThread(socket);
                client.start();
                echoThreads.add(client);
            } catch (IOException e) {
				LOG.debug("Socket connectionmanager IO Error: " + e);
            }

            try
            {
                sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        echoThreads.remove(client);
        client.halt();
    }

    public void publish(final String message)
    {
        LOG.debug("Socket connectionmanager Client count (" + echoThreads.size() + ")");

        for(EchoThread echoThread : echoThreads)
        {
            if (!echoThread.isRunning()) echoThreads.remove(echoThread);
        }

        for(EchoThread echoThread : echoThreads)
        {
            echoThread.publish(message);
        }
    }
    
    public void halt() throws IOException{
    	this.halt = true;
    	
    }

}