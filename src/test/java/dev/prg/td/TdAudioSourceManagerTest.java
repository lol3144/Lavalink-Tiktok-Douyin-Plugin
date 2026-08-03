package dev.prg.td;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TdAudioSourceManagerTest {

    @Test
    void acceptsTikTokUrls() {
        assertTrue(TdAudioSourceManager.isSupportedUrl("https://www.tiktok.com/@user/video/1234567890"));
        assertTrue(TdAudioSourceManager.isSupportedUrl("https://vm.tiktok.com/ZMabc123/"));
        assertTrue(TdAudioSourceManager.isSupportedUrl("https://vt.tiktok.com/ZMabc123/"));
    }

    @Test
    void acceptsDouyinUrls() {
        assertTrue(TdAudioSourceManager.isSupportedUrl("https://www.douyin.com/video/1234567890"));
        assertTrue(TdAudioSourceManager.isSupportedUrl("https://v.douyin.com/iExample/"));
        assertTrue(TdAudioSourceManager.isSupportedUrl("https://www.iesdouyin.com/share/video/1234567890/"));
    }

    @Test
    void ignoresUnrelatedUrls() {
        assertFalse(TdAudioSourceManager.isSupportedUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
        assertFalse(TdAudioSourceManager.isSupportedUrl("not a url"));
    }
}

