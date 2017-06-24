package org.littlewings.embedded.container.api;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpPortFinder {
    public static int find(int startPort) {
        return find(startPort, 1);
    }

    public static int find(int startPort, int range) {
        for (int i = 0; i < range; i++) {
            int port = startPort + i;

            try (ServerSocket socket = new ServerSocket(port)) {
                socket.setReuseAddress(true);

                return port;
            } catch (IOException e) {
                // ignore
            }
        }

        throw new IllegalArgumentException("could not find an available port. start-port: " + startPort + ", portRange: " + range);
    }
}
