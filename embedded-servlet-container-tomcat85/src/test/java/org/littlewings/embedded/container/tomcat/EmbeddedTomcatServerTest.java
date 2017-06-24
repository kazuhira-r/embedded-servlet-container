package org.littlewings.embedded.container.tomcat;

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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedTomcatServerTest {
    @Test
    public void test() throws URISyntaxException, IOException {
        String projectDir =
                new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI())
                        .toPath()
                        .resolve("../../../../..")
                        .normalize()
                        .toString();

        EmbeddedTomcatServer tomcat =
                EmbeddedTomcatServerBuilder
                        .create()
                        .startPort(8080)
                        .temporaryBaseDir(Paths.get(projectDir, "target/tomcat85"))
                        .contextPath("/")
                        .docBase(Paths.get(projectDir, "src/test/webapp"))
                        .build();


        tomcat.start();

        URL url = URI.create("http://localhost:" + tomcat.getPort() + "/hello").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (InputStream is = connection.getInputStream();
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            assertThat(reader.readLine()).isEqualTo("Hello World!!");
        } finally {
            connection.disconnect();
        }

        tomcat.shutdown();
    }
}
