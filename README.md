# Lavalink Douyin Plugin

Lavalink v4 source plugin for Douyin playback. It delegates extraction to a managed `yt-dlp` binary, so site compatibility can be kept current without requiring a separate system-wide install.

## Requirements

- Java 17+
- Lavalink v4.2.x
- Internet access on first startup, so the plugin can download its managed `yt-dlp` binary

## Build

```powershell
.\gradlew.bat build
```

The plugin jar is generated in `build/libs/`.

## Lavalink configuration

```yaml
lavalink:
  plugins:
    - dependency: "dev.prg:lavalink-douyin-plugin:0.1.0"
      repository: "https://jitpack.io"

plugins:
  douyin:
    ytdlp:
      managed: true
      updateOnStartup: true
      managedDirectory: "plugins/douyin-plugin"
      # Douyin often requires fresh cookies. Paste Netscape cookies.txt content here:
      cookies: |
        # Netscape HTTP Cookie File
        # https://curl.haxx.se/rfc/cookie_spec.html
        .douyin.com	TRUE	/	TRUE	0	example_cookie	example_value

      # Or point to a cookies.txt file instead:
      # cookiesFile: "cookies.txt"
      playlistLoadLimit: 100
      customLoadArgs: ["-q", "--no-warnings", "--socket-timeout", "15", "--flat-playlist", "--skip-download", "-J"]
      customPlaybackArgs: ["-q", "--no-warnings", "--socket-timeout", "15", "-f", "bestaudio/best", "-J"]
```

If you place the jar directly in Lavalink's `plugins` directory, only the `plugins.douyin` section is needed.

## Supported identifiers

- Douyin URLs such as `https://www.douyin.com/video/...`, `https://v.douyin.com/...`, `https://www.iesdouyin.com/share/video/...`
- Optional URL prefixes: `dy:<douyin-url>` or `douyin:<douyin-url>`

For best long-term support, keep `yt-dlp` updated:

`updateOnStartup: true` downloads the current official `yt-dlp` release when Lavalink starts. To use a custom local binary instead, set `managed: false` and set `path`.

## Cookie Setup

Douyin commonly requires fresh cookies. The plugin supports these cookie inputs:

- `cookies`: paste the full Netscape `cookies.txt` content into `application.yml`
- `cookiesFile`: path to a Netscape `cookies.txt` file
- `cookiesBase64`: base64 encoded Netscape `cookies.txt` content
- `cookiesEnvVar`: environment variable containing base64 encoded Netscape `cookies.txt` content

Recommended hosted setup:

```yaml
plugins:
  douyin:
    ytdlp:
      cookies: |
        # Netscape HTTP Cookie File
        # https://curl.haxx.se/rfc/cookie_spec.html
        .douyin.com	TRUE	/	TRUE	0	example_cookie	example_value
```

File-based setup:

```yaml
plugins:
  douyin:
    ytdlp:
      cookiesFile: "cookies.txt"
```

When `cookies`, `cookiesBase64`, or `cookiesEnvVar` is used, the plugin writes the cookie data to `plugins/douyin-plugin/cookies.txt` at startup and passes it to `yt-dlp`.

## yt-dlp Management

The plugin can manage its own `yt-dlp` binary. This is enabled by default:

```yaml
plugins:
  douyin:
    ytdlp:
      managed: true
      updateOnStartup: true
      managedDirectory: "plugins/douyin-plugin"
```

- `managed`: when `true`, the plugin downloads and uses its own `yt-dlp` binary. You do not need to install `yt-dlp` on the host.
- `updateOnStartup`: when `true`, the plugin checks/downloads the latest official `yt-dlp` binary whenever Lavalink starts. This helps when Douyin changes and newer `yt-dlp` support is needed.
- `managedDirectory`: directory where the plugin stores managed files, including `yt-dlp` and generated `cookies.txt`.

The default `managedDirectory: "plugins/douyin-plugin"` is relative to the Lavalink working directory. For most Lavalink setups, keep the default.

To use a manually installed binary instead:

```yaml
plugins:
  douyin:
    ytdlp:
      managed: false
      path: "/usr/local/bin/yt-dlp"
```
