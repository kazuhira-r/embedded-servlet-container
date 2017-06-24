package org.littlewings.embedded.container.jetty;

import java.nio.file.Path;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.littlewings.embedded.container.api.AbstractEmbeddedServerBuilder;

public class EmbeddedJettyServerBuilder extends AbstractEmbeddedServerBuilder<EmbeddedJettyServerBuilder, EmbeddedJettyServer> {
    protected Path webappDir;

    public static EmbeddedJettyServerBuilder create() {
        return new EmbeddedJettyServerBuilder();
    }

    public EmbeddedJettyServerBuilder webappDir(Path webappDir) {
        this.webappDir = webappDir;
        return this;
    }

    @Override
    protected EmbeddedJettyServer createActualServer() {
        Server server = new Server(port);


        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath(contextPath);
        webapp.setWar(webappDir.toAbsolutePath().toString());

        webapp.setConfigurations(new Configuration[]{
                new AnnotationConfiguration(),
                new WebXmlConfiguration(),
                new WebInfConfiguration(),
                new PlusConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration(),
                new EnvConfiguration()});

        server.setHandler(webapp);

        return new EmbeddedJettyServer(server);
    }
}
