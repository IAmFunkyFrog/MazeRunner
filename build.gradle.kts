import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("org.openjfx.javafxplugin") version "0.0.13"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

application {
    mainClass.set("com.mazerunner.app.MazeRunnerApp")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("no.tornado:tornadofx:2.0.0-SNAPSHOT")
}

javafx {
    modules("javafx.controls")
}

java {
    modularity.inferModulePath.set(true)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}