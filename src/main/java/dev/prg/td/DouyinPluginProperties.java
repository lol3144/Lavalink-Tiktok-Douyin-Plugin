package dev.prg.td;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "plugins.douyin")
public class DouyinPluginProperties {

    private final Ytdlp ytdlp = new Ytdlp();

    public Ytdlp getYtdlp() {
        return ytdlp;
    }

    public static class Ytdlp {
        private String path = "yt-dlp";
        private boolean managed = true;
        private boolean updateOnStartup = true;
        private String managedDirectory = "plugins/douyin-plugin";
        private int searchLimit = 10;
        private int playlistLoadLimit = 100;
        private String cookiesFile;
        private String cookies;
        private String cookiesBase64;
        private String cookiesEnvVar = "DOUYIN_COOKIES_BASE64";
        private String cookiesFromBrowser;
        private String resolvedCookiesFile;
        private String[] customLoadArgs = new String[]{"-q", "--no-warnings", "--socket-timeout", "15", "--flat-playlist", "--skip-download", "-J"};
        private String[] customPlaybackArgs = new String[]{"-q", "--no-warnings", "--socket-timeout", "15", "-f", "bestaudio/best", "-J"};

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isManaged() {
            return managed;
        }

        public void setManaged(boolean managed) {
            this.managed = managed;
        }

        public boolean isUpdateOnStartup() {
            return updateOnStartup;
        }

        public void setUpdateOnStartup(boolean updateOnStartup) {
            this.updateOnStartup = updateOnStartup;
        }

        public String getManagedDirectory() {
            return managedDirectory;
        }

        public void setManagedDirectory(String managedDirectory) {
            this.managedDirectory = managedDirectory;
        }

        public int getSearchLimit() {
            return searchLimit;
        }

        public void setSearchLimit(int searchLimit) {
            this.searchLimit = searchLimit;
        }

        public int getPlaylistLoadLimit() {
            return playlistLoadLimit;
        }

        public void setPlaylistLoadLimit(int playlistLoadLimit) {
            this.playlistLoadLimit = playlistLoadLimit;
        }

        public String getCookiesFile() {
            return cookiesFile;
        }

        public void setCookiesFile(String cookiesFile) {
            this.cookiesFile = cookiesFile;
        }

        public String getCookies() {
            return cookies;
        }

        public void setCookies(String cookies) {
            this.cookies = cookies;
        }

        public String getCookiesBase64() {
            return cookiesBase64;
        }

        public void setCookiesBase64(String cookiesBase64) {
            this.cookiesBase64 = cookiesBase64;
        }

        public String getCookiesEnvVar() {
            return cookiesEnvVar;
        }

        public void setCookiesEnvVar(String cookiesEnvVar) {
            this.cookiesEnvVar = cookiesEnvVar;
        }

        public String getCookiesFromBrowser() {
            return cookiesFromBrowser;
        }

        public void setCookiesFromBrowser(String cookiesFromBrowser) {
            this.cookiesFromBrowser = cookiesFromBrowser;
        }

        public String getResolvedCookiesFile() {
            return resolvedCookiesFile;
        }

        public void setResolvedCookiesFile(String resolvedCookiesFile) {
            this.resolvedCookiesFile = resolvedCookiesFile;
        }

        public String[] getCustomLoadArgs() {
            return customLoadArgs;
        }

        public void setCustomLoadArgs(String[] customLoadArgs) {
            this.customLoadArgs = customLoadArgs;
        }

        public String[] getCustomPlaybackArgs() {
            return customPlaybackArgs;
        }

        public void setCustomPlaybackArgs(String[] customPlaybackArgs) {
            this.customPlaybackArgs = customPlaybackArgs;
        }

        public String[] getEffectiveCustomLoadArgs() {
            return withCookieArgs(customLoadArgs);
        }

        public String[] getEffectiveCustomPlaybackArgs() {
            return withCookieArgs(customPlaybackArgs);
        }

        private String[] withCookieArgs(String[] baseArgs) {
            List<String> args = new ArrayList<>(Arrays.asList(baseArgs));
            if (hasText(resolvedCookiesFile)) {
                args.add("--cookies");
                args.add(resolvedCookiesFile);
            }
            if (hasText(cookiesFromBrowser)) {
                args.add("--cookies-from-browser");
                args.add(cookiesFromBrowser);
            }
            return args.toArray(String[]::new);
        }

        private static boolean hasText(String value) {
            return value != null && !value.isBlank();
        }
    }
}
