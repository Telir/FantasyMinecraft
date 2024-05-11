//import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.22"
//    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "Telir"
version = "1.0.0-SNAPSHOT"
description = "Fantasy Minecraft!"


repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    implementation(files("C:/Users/local-admin/.m2/repository/org/spigotmc/spigot/1.12.2-R0.1-SNAPSHOT/spigot-1.12.2-R0.1-SNAPSHOT.jar"))

    implementation("org.mongodb:mongodb-driver-sync:5.0.1")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("org.slf4j:slf4j-simple:2.0.13")
}

//val shadowJar by configurations.creating
//
//dependencies {
//    shadowJar(files("C:/Users/local-admin/.m2/repository/org/spigotmc/spigot/1.12.2-R0.1-SNAPSHOT/spigot-1.12.2-R0.1-SNAPSHOT.jar"))
//    shadowJar("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
//}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
//     val shadow by creating(ShadowJar::class) {
//        archiveFileName.set("C:/Users/local-admin/Desktop/MPD2/server/plugins/Dota2Minecraft-${project.version}.jar")
//        dependencies {
//            from(shadowJar)
//        }
//    }

    val copyJar by creating(Copy::class) {
        dependsOn(build)
        val outputJarFile = layout.buildDirectory.file("libs/FantasyMinecraft-${project.version}.jar").get().asFile
        from(outputJarFile)
        into("C:/Users/local-admin/Desktop/MPD2/server/plugins/")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    jar {
        from(sourceSets["main"].resources)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(configurations["runtimeClasspath"].map { if (it.isDirectory) it else zipTree(it) })
    }

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}