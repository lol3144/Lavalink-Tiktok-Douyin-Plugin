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
    - dependency: "dev.prg:lavalink-douyin-plugin:0.1.2"
      repository: "https://jitpack.io"

plugins:
  douyin:
    ytdlp:
      managed: true
      updateOnStartup: true
      # Douyin often requires fresh guest verification cookies. Login is not required.
      # Paste browser/incognito-exported Netscape cookies.txt content here:
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

Douyin commonly requires fresh guest verification cookies. A Douyin account login is not required, but Douyin's browser challenge often creates short-lived guest cookies even in incognito/private mode. Server-hosted Lavalink cannot run that browser challenge by itself, so export the fresh Netscape `cookies.txt` from a browser session where the target video opens and provide it to the plugin.

The plugin supports these cookie inputs:

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

Guest-cookie flow:

1. Open the Douyin video in a normal or incognito/private browser window.
2. Export cookies for `douyin.com` in Netscape `cookies.txt` format.
3. Paste the full file into `plugins.douyin.ytdlp.cookies`, or point `cookiesFile` at that file.

File-based setup:

```yaml
plugins:
  douyin:
    ytdlp:
      cookiesFile: "cookies.txt"
```

When `cookies`, `cookiesBase64`, or `cookiesEnvVar` is used, the plugin writes the cookie data to its internal managed directory at startup and passes it to `yt-dlp`.

## yt-dlp Management

The plugin can manage its own `yt-dlp` binary. This is enabled by default:

```yaml
plugins:
  douyin:
    ytdlp:
      managed: true
      updateOnStartup: true
```

- `managed`: when `true`, the plugin downloads and uses its own `yt-dlp` binary. You do not need to install `yt-dlp` on the host.
- `updateOnStartup`: when `true`, the plugin checks/downloads the latest official `yt-dlp` binary whenever Lavalink starts. This helps when Douyin changes and newer `yt-dlp` support is needed.

By default, managed files are stored in the JVM temp directory under `lavalink-douyin-plugin`. You normally do not need to configure a folder. On Linux, the plugin downloads the standalone `yt-dlp_linux` binary so Python is not required on the host.

If your host clears temp files often and you want a persistent cache, you can set `managedDirectory` manually:

```yaml
plugins:
  douyin:
    ytdlp:
      managedDirectory: "plugins/douyin-plugin"
```

To use a manually installed binary instead:

```yaml
plugins:
  douyin:
    ytdlp:
      managed: false
      path: "/usr/local/bin/yt-dlp"
```
