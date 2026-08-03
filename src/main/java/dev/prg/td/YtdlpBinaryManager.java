package dev.prg.td;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Locale;

final class YtdlpBinaryManager {

    private static final Logger log = LoggerFactory.getLogger(YtdlpBinaryManager.class);
    private static final String RELEASE_BASE = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/";

    private YtdlpBinaryManager() {
    }

    static String resolve(DouyinPluginProperties.Ytdlp properties) {
        if (!properties.isManaged()) {
            return properties.getPath();
        }

        Path directory = Path.of(properties.getManagedDirectory()).toAbsolutePath().normalize();
        String fileName = isWindows() ? "yt-dlp.exe" : "yt-dlp";
        Path binary = directory.resolve(fileName);

        if (properties.isUpdateOnStartup() || Files.notExists(binary)) {
            try {
                download(binary, RELEASE_BASE + fileName);
            } catch (RuntimeException e) {
                if (Files.exists(binary)) {
                    log.warn("Could not update managed yt-dlp, using existing binary at {}", binary, e);
                } else {
                    throw e;
                }
            }
        }

        makeExecutable(binary);
        return binary.toString();
    }

    private static void download(Path target, String url) {
        Path temp = target.resolveSibling(target.getFileName() + ".tmp");
        try {
            Files.createDirectories(target.getParent());

            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofMinutes(3))
                .header("User-Agent", "lavalink-douyin-plugin")
                .GET()
                .build();

            log.info("Downloading managed yt-dlp binary from {}", url);
            HttpResponse<InputStream> response = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(30))
                .build()
                .send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("yt-dlp download failed with HTTP " + response.statusCode());
            }

            try (InputStream body = response.body()) {
                Files.copy(body, temp, StandardCopyOption.REPLACE_EXISTING);
            }

            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            log.info("Managed yt-dlp binary is ready at {}", target);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to download yt-dlp to " + target, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while downloading yt-dlp", e);
        } finally {
            try {
                Files.deleteIfExists(temp);
            } catch (IOException ignored) {
            }
        }
    }

    private static void makeExecutable(Path binary) {
        if (!isWindows()) {
            binary.toFile().setExecutable(true, false);
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win");
    }
}
