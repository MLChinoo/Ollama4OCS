plugins {
    id("application")
    id("java")
    id("io.freefair.lombok") version "8.13.1"
}

group = "work.mlchinoo.ollama4ocs"
version = "1.0.0"

application {
    mainClass = "work.mlchinoo.ollama4ocs.Main"
    applicationDefaultJvmArgs = listOf("-Djansi.passthrough=true")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("io.javalin:javalin:6.5.0")
    implementation("org.fusesource.jansi:jansi:2.4.1")
    implementation("com.google.code.gson:gson:2.12.1")
    // implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.apache.commons:commons-text:1.13.0")
    implementation("io.github.ollama4j:ollama4j:1.0.100")
}
