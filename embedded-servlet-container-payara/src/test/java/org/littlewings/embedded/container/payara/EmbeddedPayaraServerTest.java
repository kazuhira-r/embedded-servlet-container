package org.littlewings.embedded.container.payara;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmbeddedPayaraServerTest {
    @Test
    void test() throws URISyntaxException, IOException {
        String currentDir =
                new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI())
                        .toPath()
                        .resolve("../..")
                        .normalize()
                        .toString();

        EmbeddedPayaraServer payara =
                EmbeddedPayaraServerBuilder
                        .create()
                        .startPort(8080)
                        .contextPath("/")
                        .deployDir(Paths.get("src/test/webapp"))
                        .applicationName("sample")
                        .build();


        payara.start();

        URL url = URI.create("http://localhost:" + payara.getPort() + "/hello").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (InputStream is = connection.getInputStream();
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            assertThat(reader.readLine()).isEqualTo("Hello World!!");
        } finally {
            connection.disconnect();
        }

        payara.shutdown();
    }
}
