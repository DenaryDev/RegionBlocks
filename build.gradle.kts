plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

group = "io.sapphiremc.regionblocks"
version = "1.0.1"

repositories {
    mavenCentral()
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies  {
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:6.1.4-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-legacy:6.2")
    compileOnly("org.projectlombok:lombok:1.18.22")

    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    compileJava {
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

    jar {
        archiveBaseName.set("RegionBlocks")
    }

    processResources {
        filesMatching(listOf("plugin.yml", "config.yml")) {
            expand("version" to project.version)
        }
    }

    runServer {
        minecraftVersion("1.12.2")
        if (!System.getenv("useCustomCore").isNullOrEmpty()) {
            serverJar.set(project.projectDir.resolve("run/server.jar"))
        }
    }
}