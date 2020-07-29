import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    id("com.jfrog.bintray") version "1.8.5"
    id("maven-publish")
    kotlin("jvm") version "1.3.72"
}

group = "io.github.abansod"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    val junitVersion = "5.3.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    runtimeOnly("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("io.kotlintest:kotlintest-assertions:3.1.7")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.5")

}

tasks.withType<Test> {
    useJUnitPlatform()
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}


task("sourceJar", Jar::class) {
    description = "Assembles source JAR"
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.jar.get().dependsOn(tasks["sourceJar"])

publishing {
    publications {
        register<MavenPublication>("kotora-core") {
            from(components["java"])
            artifact(tasks["sourceJar"])
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_API_KEY")
    setPublications("kotora-core")
    publish = true
    override = false
    pkg(closureOf<BintrayExtension.PackageConfig> {
        repo = "kotora"
        name = "kotora"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/abansod/Kotora.git"
        setLabels("kotlin", "kotora")
        publicDownloadNumbers = true
    })
}