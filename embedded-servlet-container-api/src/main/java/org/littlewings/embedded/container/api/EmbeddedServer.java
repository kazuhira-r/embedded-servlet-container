package org.littlewings.embedded.container.api;

public interface EmbeddedServer<S extends EmbeddedServer<?>> {
    S start();

    int getPort();

    void await();

    void shutdown();
}
