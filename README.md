# Lavalink TikTok/Douyin Plugin

Lavalink v4 source plugin for TikTok and Douyin playback. It delegates extraction to `yt-dlp`, so site compatibility can be kept current by updating `yt-dlp` without rebuilding the plugin.

## Requirements

- Java 17+
- Lavalink v4.2.x
- `yt-dlp` available on `PATH`, or configured with an absolute path

## Build

```powershell
.\gradlew.bat build
```

The plugin jar is generated in `build/libs/`.

## Lavalink configuration

```yaml
lavalink:
  plugins:
    - dependency: "dev.prg:lavalink-td-plugin:0.1.0"
      repository: "https://jitpack.io"

plugins:
  td:
    ytdlp:
      path: "yt-dlp"
      playlistLoadLimit: 100
      customLoadArgs: ["-q", "--no-warnings", "--flat-playlist", "--skip-download", "-J"]
      customPlaybackArgs: ["-q", "--no-warnings", "-f", "bestaudio/best", "-J"]
```

If you place the jar directly in Lavalink's `plugins` directory, only the `plugins.td` section is needed.

## Supported identifiers

- TikTok URLs such as `https://www.tiktok.com/@user/video/...`, `https://vm.tiktok.com/...`, `https://vt.tiktok.com/...`
- Douyin URLs such as `https://www.douyin.com/video/...`, `https://v.douyin.com/...`, `https://www.iesdouyin.com/share/video/...`
- Optional URL prefix: `td:<tiktok-or-douyin-url>`

For best long-term support, keep `yt-dlp` updated:

```powershell
yt-dlp -U
```
