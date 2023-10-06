plugins {
    java
    id("xyz.jpenilla.run-paper") version "2.0.1"
}

group = "me.rafaelka"
version = "1.2.0"

repositories {
    mavenCentral()
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies  {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.15")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.8")
    compileOnly("org.projectlombok:lombok:1.18.26")

    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    compileJava {
        options.release.set(17)
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.addAll(
            listOf(
                "-parameters",
                "-nowarn",
                "-Xlint:-unchecked",
                "-Xlint:-deprecation",
                "-Xlint:-processing"
            )
        )
        options.isFork = true
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching(listOf("plugin.yml", "config.yml")) {
            expand("version" to project.version)
        }
    }

    runServer {
        minecraftVersion("1.20.1")
    }
}