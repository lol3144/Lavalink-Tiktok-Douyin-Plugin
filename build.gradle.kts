plugins {
    java
    alias(libs.plugins.lavalink)
}

group = "dev.prg"
version = "0.1.0"

lavalinkPlugin {
    name = "douyin-plugin"
    apiVersion = libs.versions.lavalink.api
    serverVersion = libs.versions.lavalink.server
}

repositories {
    mavenCentral()
    maven("https://maven.lavalink.dev/releases")
    maven("https://maven.lavalink.dev/snapshots")
    maven("https://maven.topi.wtf/releases")
    maven("https://maven.topi.wtf/snapshots")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(libs.lavasrc)
    compileOnly(libs.lavaplayer)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testCompileOnly(libs.lavaplayer)
    testRuntimeOnly(libs.lavaplayer)
}
