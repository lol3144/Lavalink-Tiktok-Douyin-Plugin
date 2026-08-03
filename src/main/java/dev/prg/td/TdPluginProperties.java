package dev.prg.td;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "plugins.td")
public class TdPluginProperties {

    private final Ytdlp ytdlp = new Ytdlp();

    public Ytdlp getYtdlp() {
        return ytdlp;
    }

    public static class Ytdlp {
        private String path = "yt-dlp";
        private int searchLimit = 10;
        private int playlistLoadLimit = 100;
        private String[] customLoadArgs = new String[]{"-q", "--no-warnings", "--flat-playlist", "--skip-download", "-J"};
        private String[] customPlaybackArgs = new String[]{"-q", "--no-warnings", "-f", "bestaudio/best", "-J"};

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
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
    }
}

