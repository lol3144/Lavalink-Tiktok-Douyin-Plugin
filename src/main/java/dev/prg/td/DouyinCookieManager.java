package dev.prg.td;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

final class DouyinCookieManager {

    private static final Logger log = LoggerFactory.getLogger(DouyinCookieManager.class);

    private DouyinCookieManager() {
    }

    static void resolve(DouyinPluginProperties.Ytdlp properties) {
        if (hasText(properties.getCookiesFile())) {
            properties.setResolvedCookiesFile(properties.getCookiesFile());
            return;
        }

        String cookieData = firstText(
            properties.getCookies(),
            decodeBase64(firstText(properties.getCookiesBase64(), System.getenv(properties.getCookiesEnvVar())))
        );

        if (!hasText(cookieData)) {
            return;
        }

        Path target = YtdlpBinaryManager.managedDirectory(properties).resolve("cookies.txt");
        try {
            Files.createDirectories(target.getParent());
            Files.writeString(target, cookieData, StandardCharsets.UTF_8);
            properties.setResolvedCookiesFile(target.toString());
            log.info("Douyin cookies loaded from configuration into {}", target);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write Douyin cookies to " + target, e);
        }
    }

    private static String decodeBase64(String value) {
        if (!hasText(value)) {
            return null;
        }

        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }

    private static String firstText(String first, String second) {
        return hasText(first) ? first : second;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
