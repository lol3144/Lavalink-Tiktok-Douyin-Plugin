package dev.prg.td;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import dev.arbjerg.lavalink.api.AudioPlayerManagerConfiguration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(DouyinPluginProperties.class)
public class DouyinPlugin implements AudioPlayerManagerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DouyinPlugin.class);
    private final DouyinAudioSourceManager sourceManager;

    public DouyinPlugin(DouyinPluginProperties properties) {
        String ytdlpPath = YtdlpBinaryManager.resolve(properties.getYtdlp());
        DouyinCookieManager.resolve(properties.getYtdlp());
        this.sourceManager = new DouyinAudioSourceManager(properties, ytdlpPath);
        log.info("Douyin source initialized with yt-dlp executable: {}", ytdlpPath);
    }

    @NotNull
    @Override
    public AudioPlayerManager configure(@NotNull AudioPlayerManager manager) {
        log.info("Registering Douyin audio source manager...");
        manager.registerSourceManager(sourceManager);
        return manager;
    }
}
