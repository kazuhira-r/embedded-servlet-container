package org.littlewings.embedded.container.tomcat;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.littlewings.embedded.container.api.AbstractEmbeddedServerBuilder;

public class EmbeddedTomcatServerBuilder extends AbstractEmbeddedServerBuilder<EmbeddedTomcatServerBuilder, EmbeddedTomcatServer> {
    protected Path baseDir;
    protected Path docBase;

    public static EmbeddedTomcatServerBuilder create() {
        return new EmbeddedTomcatServerBuilder();
    }

    public EmbeddedTomcatServerBuilder baseDir(Path baseDir) {
        this.baseDir = baseDir;
        return this;
    }

    public EmbeddedTomcatServerBuilder docBase(Path docBase) {
        this.docBase = docBase;
        return this;
    }

    public EmbeddedTomcatServerBuilder temporaryBaseDir() {
        return temporaryBaseDir(Paths.get(System.getProperty("java.io.tmpdir")));
    }

    public EmbeddedTomcatServerBuilder temporaryBaseDir(Path dir) {
        baseDir = createTemporaryDirectory(dir, "tomcat");
        return this;
    }

    @Override
    protected EmbeddedTomcatServer createActualServer() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(baseDir.toString());
        tomcat.getServer().setParentClassLoader(getClass().getClassLoader());

        Context context;
        try {
            context = tomcat.addWebapp(contextPath, docBase.toString());
        } catch (ServletException e) {
            throw new IllegalStateException(e);
        }

        return new EmbeddedTomcatServer(tomcat, context);
    }
}
