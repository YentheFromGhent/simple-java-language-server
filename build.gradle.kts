plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.yenthefromghent"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-cli:commons-cli:1.5.0")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.yenthefromghent.sjls.Main",
        )
    }
}

tasks.shadowJar {
    archiveBaseName.set("js_ls")
    archiveVersion.set("1.0.0")
    manifest {
        attributes(mapOf("Main-Class" to "com.yenthefromghent.sjls.Main"))
    }
}