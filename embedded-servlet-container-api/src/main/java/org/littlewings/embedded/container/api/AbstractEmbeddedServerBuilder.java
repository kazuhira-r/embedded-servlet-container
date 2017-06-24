package org.littlewings.embedded.container.api;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public abstract class AbstractEmbeddedServerBuilder<B extends EmbeddedServerBuilder<?, ?>, S extends EmbeddedServer<?>> implements EmbeddedServerBuilder<B, S> {
    protected int port;
    protected String contextPath;

    protected int startPort;
    protected int portRange;

    @Override
    public B startPort(int startPort) {
        return startPort(startPort, 1000);
    }

    @SuppressWarnings("unchecked")
    @Override
    public B startPort(int startPort, int range) {
        this.startPort = startPort;
        this.portRange = range;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public B contextPath(String contextPath) {
        this.contextPath = contextPath;
        return (B) this;
    }

    @Override
    public S build() {
        if (portRange < 1) {
            port = TcpPortFinder.find(startPort);
        } else {
            port = TcpPortFinder.find(startPort, portRange);
        }

        return createActualServer();
    }

    protected abstract S createActualServer();

    public static Path createTemporaryDirectory(Path dir, String prefix) {
        try {
            Files.createDirectories(dir);
            Path temporaryDirectory = Files.createTempDirectory(dir, prefix);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Files.walkFileTree(temporaryDirectory, new SimpleFileVisitor<Path>() {
                                @Override
                                public FileVisitResult visitFile(Path file,
                                                                 BasicFileAttributes attrs) throws IOException {
                                    Files.delete(file);
                                    return FileVisitResult.CONTINUE;
                                }

                                @Override
                                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                                        throws IOException {
                                    Files.delete(dir);
                                    return FileVisitResult.CONTINUE;
                                }
                            }
                    );
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }));

            return temporaryDirectory.toAbsolutePath();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
