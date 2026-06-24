plugins {
    kotlin("jvm") version "2.0.20"
    application
}

group = "com.porek"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("dev.kord:kord-core:0.14.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}