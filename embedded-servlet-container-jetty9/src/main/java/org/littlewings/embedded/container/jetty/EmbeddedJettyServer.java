package org.littlewings.embedded.container.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.littlewings.embedded.container.api.EmbeddedServer;

public class EmbeddedJettyServer implements EmbeddedServer<EmbeddedJettyServer> {
    protected Server server;

    protected EmbeddedJettyServer(Server server) {
        this.server = server;
    }

    @Override
    public EmbeddedJettyServer start() {
        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    @Override
    public int getPort() {
        return ((ServerConnector) server.getConnectors()[0]).getPort();
    }

    @Override
    public void await() {
        try {
            server.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void shutdown() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
