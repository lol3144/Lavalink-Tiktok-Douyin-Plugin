package dev.prg.td;

import com.github.topi314.lavasrc.ytdlp.YtdlpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class TdAudioSourceManager extends YtdlpAudioSourceManager {

    public static final String SEARCH_PREFIX = "td:";

    private static final Pattern SUPPORTED_URL = Pattern.compile(
        "^https?://(?:(?:www|m|vm|vt|v)\\.)?(?:tiktok\\.com|douyin\\.com|iesdouyin\\.com|snssdk\\.com|amemv\\.com|ixigua\\.com)/.+",
        Pattern.CASE_INSENSITIVE
    );

    public TdAudioSourceManager(TdPluginProperties properties) {
        super(
            properties.getYtdlp().getPath(),
            properties.getYtdlp().getSearchLimit(),
            properties.getYtdlp().getPlaylistLoadLimit(),
            properties.getYtdlp().getPlaylistLoadLimit(),
            properties.getYtdlp().getCustomLoadArgs(),
            properties.getYtdlp().getCustomPlaybackArgs()
        );
    }

    @NotNull
    @Override
    public String getSourceName() {
        return "td";
    }

    @Override
    public AudioItem loadItem(AudioPlayerManager manager, AudioReference reference) {
        String identifier = reference.identifier;
        if (identifier == null || identifier.isBlank()) {
            return null;
        }

        try {
            if (identifier.regionMatches(true, 0, SEARCH_PREFIX, 0, SEARCH_PREFIX.length())) {
                identifier = identifier.substring(SEARCH_PREFIX.length()).trim();
                if (!isSupportedUrl(identifier)) {
                    return AudioReference.NO_TRACK;
                }
            }

            if (isSupportedUrl(identifier)) {
                return getItem(identifier);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load TikTok/Douyin item with yt-dlp", e);
        }

        return null;
    }

    static boolean isSupportedUrl(String identifier) {
        return SUPPORTED_URL.matcher(identifier.toLowerCase(Locale.ROOT)).matches();
    }
}
