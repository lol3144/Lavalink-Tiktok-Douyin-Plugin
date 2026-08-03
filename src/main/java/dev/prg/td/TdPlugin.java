package dev.prg.td;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(TdPluginProperties.class)
public class TdPlugin {

    private static final Logger log = LoggerFactory.getLogger(TdPlugin.class);

    public TdPlugin(TdPluginProperties properties) {
        log.info("TikTok/Douyin source enabled with yt-dlp executable: {}", properties.getYtdlp().getPath());
    }
}

