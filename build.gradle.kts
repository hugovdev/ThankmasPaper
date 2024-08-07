plugins {
    kotlin("jvm") version "1.9.21"
    id("io.papermc.paperweight.userdev") version "1.5.11"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
}

group = "me.hugo.thankmas"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven(url = "https://jitpack.io")
    maven(url = "https://repo.papermc.io/repository/maven-public/")
    maven(url = "https://nexus.leonardbausenwein.de/repository/maven-public/")
    maven(url = "https://repo.infernalsuite.com/repository/maven-snapshots/")
    maven(url = "https://repo.rapture.pw/repository/maven-releases/")
    maven(url = "https://maven.noxcrew.com/public/")
    maven(url = "https://maven.citizensnpcs.co/repo")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    // LuckPerms API for nice access to groups.
    compileOnly("net.luckperms:api:5.4")

    // Citizens API
    compileOnly("net.citizensnpcs:citizens-main:2.0.33-SNAPSHOT") {
        exclude(mutableMapOf("group" to "*", "module" to "*"))
    }

    // Dependency Injection framework.
    ksp("io.insert-koin:koin-ksp-compiler:1.3.1")

    // Nice scoreboard API.
    implementation("fr.mrmicky:fastboard:2.1.3")

    // Main Thankmas framework.
    implementation(files("C:/Users/hugov/IdeaProjects/TranslationsTest/build/libs/Thankmas-1.0-SNAPSHOT-all.jar"))

    // Minecraft UI interface framework from the absolute goats at Noxcrew.
    implementation("com.noxcrew.interfaces:interfaces:1.0.1")

    // Nice command framework.
    implementation("com.github.Revxrsal.Lamp:common:3.2.1")
    implementation("com.github.Revxrsal.Lamp:bukkit:3.2.1")

    // Library to access minecraft world files.
    implementation("io.github.jglrxavpok.hephaistos:common:2.6.1")

    // Amazon S3 API for nice world downloading and uploading :)
    implementation(platform("software.amazon.awssdk:bom:2.26.18"))
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:sso")
    implementation("software.amazon.awssdk:ssooidc")
    implementation("software.amazon.awssdk:apache-client")

    // Zip library
    implementation("org.zeroturnaround:zt-zip:1.17")

    // Slime worlds APIs.
    compileOnly("com.infernalsuite.aswm:api:1.20.4-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}

tasks.shadowJar {
    relocate("fr.mrmicky.fastboard", "me.hugo.thankmas.fastboard")
}

tasks.compileKotlin {
    kotlinOptions.javaParameters = true
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}