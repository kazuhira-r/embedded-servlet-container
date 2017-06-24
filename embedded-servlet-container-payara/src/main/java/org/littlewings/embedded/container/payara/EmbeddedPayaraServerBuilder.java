package org.littlewings.embedded.container.payara;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.glassfish.embeddable.BootstrapProperties;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.littlewings.embedded.container.api.AbstractEmbeddedServerBuilder;

public class EmbeddedPayaraServerBuilder extends AbstractEmbeddedServerBuilder<EmbeddedPayaraServerBuilder, EmbeddedPayaraServer> {
    protected Path deployDir;
    protected String applicationName;

    public static EmbeddedPayaraServerBuilder create() {
        return new EmbeddedPayaraServerBuilder();
    }

    public EmbeddedPayaraServerBuilder deployDir(Path deployDir) {
        this.deployDir = deployDir;
        return this;
    }

    public EmbeddedPayaraServerBuilder applicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    @Override
    protected EmbeddedPayaraServer createActualServer() {
        try {
            BootstrapProperties bootstrap = new BootstrapProperties();
            GlassFishRuntime runtime = GlassFishRuntime.bootstrap(bootstrap);
            GlassFishProperties properties = new GlassFishProperties();
            properties.setPort("http-listener", port);

            GlassFish glassfish = runtime.newGlassFish(properties);

            Consumer<GlassFish> deployer = glassFish -> {
                try {
                    glassFish
                            .getDeployer()
                            .deploy(
                                    deployDir.toFile(),
                                    "--contextroot",
                                    contextPath,
                                    "--name",
                                    applicationName
                            );
                } catch (GlassFishException e) {
                    throw new IllegalStateException(e);
                }
            };

            return new EmbeddedPayaraServer(glassfish, properties, deployer);
        } catch (GlassFishException e) {
            throw new IllegalStateException(e);
        }
    }
}
