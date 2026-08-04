package dev.prg.td;

import com.github.topi314.lavasrc.ytdlp.YtdlpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

public class DouyinAudioSourceManager extends YtdlpAudioSourceManager {

    public static final String SHORT_PREFIX = "dy:";
    public static final String LONG_PREFIX = "douyin:";

    private static final Pattern SUPPORTED_URL = Pattern.compile(
        "^https?://(?:(?:www|m|v)\\.)?(?:douyin\\.com|iesdouyin\\.com|snssdk\\.com|amemv\\.com)/.+",
        Pattern.CASE_INSENSITIVE
    );

    public DouyinAudioSourceManager(DouyinPluginProperties properties, String ytdlpPath) {
        super(
            ytdlpPath,
            properties.getYtdlp().getSearchLimit(),
            properties.getYtdlp().getPlaylistLoadLimit(),
            properties.getYtdlp().getPlaylistLoadLimit(),
            properties.getYtdlp().getEffectiveCustomLoadArgs(),
            properties.getYtdlp().getEffectiveCustomPlaybackArgs()
        );
    }

    @NotNull
    @Override
    public String getSourceName() {
        return "douyin";
    }

    @Override
    public AudioItem loadItem(AudioPlayerManager manager, AudioReference reference) {
        String identifier = reference.identifier;
        if (identifier == null || identifier.isBlank()) {
            return null;
        }

        try {
            identifier = unwrapPrefix(identifier.trim());
            if (identifier == null) {
                return AudioReference.NO_TRACK;
            }

            if (isSupportedUrl(identifier)) {
                return getItem(identifier);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Douyin item with yt-dlp", e);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("Fresh cookies")) {
                throw new FriendlyException(
                    "Douyin requires fresh guest verification cookies. Login is not required; paste incognito/browser-exported Netscape cookies into plugins.douyin.ytdlp.cookies or set cookiesFile.",
                    FriendlyException.Severity.COMMON,
                    e
                );
            }
            throw e;
        }

        return null;
    }

    static boolean isSupportedUrl(String identifier) {
        return SUPPORTED_URL.matcher(identifier.toLowerCase(Locale.ROOT)).matches();
    }

    private static String unwrapPrefix(String identifier) {
        if (identifier.regionMatches(true, 0, SHORT_PREFIX, 0, SHORT_PREFIX.length())) {
            return prefixedUrl(identifier, SHORT_PREFIX);
        }
        if (identifier.regionMatches(true, 0, LONG_PREFIX, 0, LONG_PREFIX.length())) {
            return prefixedUrl(identifier, LONG_PREFIX);
        }
        return identifier;
    }

    private static String prefixedUrl(String identifier, String prefix) {
        String url = identifier.substring(prefix.length()).trim();
        return isSupportedUrl(url) ? url : null;
    }
}
