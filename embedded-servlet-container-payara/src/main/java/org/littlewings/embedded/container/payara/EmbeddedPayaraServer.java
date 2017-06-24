package org.littlewings.embedded.container.payara;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.littlewings.embedded.container.api.EmbeddedServer;

public class EmbeddedPayaraServer implements EmbeddedServer<EmbeddedPayaraServer> {
    protected GlassFish glassfish;
    protected GlassFishProperties properties;
    protected Consumer<GlassFish> deployer;

    private volatile boolean await = false;

    protected EmbeddedPayaraServer(GlassFish glassfish, GlassFishProperties properties, Consumer<GlassFish> deployer) {
        this.glassfish = glassfish;
        this.properties = properties;
        this.deployer = deployer;
    }

    @Override
    public EmbeddedPayaraServer start() {
        try {
            glassfish.start();
            deployer.accept(glassfish);
        } catch (GlassFishException e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    @Override
    public int getPort() {
        return properties.getPort("http-listener");
    }

    @Override
    public void await() {
        await = true;

        while (await) {
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    @Override
    public void shutdown() {
        try {
            await = false;

            glassfish.stop();
            glassfish.dispose();
        } catch (GlassFishException e) {
            throw new IllegalStateException(e);
        }
    }
}
