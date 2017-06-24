package org.littlewings.embedded.container.api;

public interface EmbeddedServerBuilder<B extends EmbeddedServerBuilder<?, ?>, S extends EmbeddedServer<?>> {
    B startPort(int startPort);

    B startPort(int startPort, int range);

    B contextPath(String contextPath);

    S build();
}
