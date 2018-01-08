package org.littlewings.embedded.container.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.littlewings.embedded.container.api.EmbeddedServer;

public class EmbeddedTomcatServer implements EmbeddedServer<EmbeddedTomcatServer> {
    protected Tomcat tomcat;
    protected Context context;

    protected EmbeddedTomcatServer(Tomcat tomcat, Context context) {
        this.tomcat = tomcat;
        this.context = context;
    }

    @Override
    public int getPort() {
        return tomcat.getConnector().getLocalPort();
    }

    @Override
    public EmbeddedTomcatServer start() {
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new IllegalStateException(e);
        }

        return this;
    }

    @Override
    public void await() {
        tomcat.getServer().await();
    }

    @Override
    public void shutdown() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new IllegalStateException(e);
        }
    }
}
