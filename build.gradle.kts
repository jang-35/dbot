plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

application.mainClass = "com.dbot.Bot" //
group = "org.example"
version = "1.0"

val jdaVersion = "5.0.0-beta.13" //

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:1.2.8")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.apache.commons:commons-lang3:3.13.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    // Set this to the version of java you want to use,
    // the minimum required for JDA is 1.8
    sourceCompatibility = "1.8"
}