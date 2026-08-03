package dev.prg.td;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DouyinAudioSourceManagerTest {

    @Test
    void acceptsDouyinUrls() {
        assertTrue(DouyinAudioSourceManager.isSupportedUrl("https://www.douyin.com/video/1234567890"));
        assertTrue(DouyinAudioSourceManager.isSupportedUrl("https://v.douyin.com/iExample/"));
        assertTrue(DouyinAudioSourceManager.isSupportedUrl("https://www.iesdouyin.com/share/video/1234567890/"));
    }

    @Test
    void rejectsTikTokUrls() {
        assertFalse(DouyinAudioSourceManager.isSupportedUrl("https://www.tiktok.com/@user/video/1234567890"));
        assertFalse(DouyinAudioSourceManager.isSupportedUrl("https://vm.tiktok.com/ZMabc123/"));
    }

    @Test
    void ignoresUnrelatedUrls() {
        assertFalse(DouyinAudioSourceManager.isSupportedUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
        assertFalse(DouyinAudioSourceManager.isSupportedUrl("not a url"));
    }
}

