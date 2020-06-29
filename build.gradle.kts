plugins {
    kotlin("jvm") version "1.3.72"
}

group = "org.funKoroutine"
version = "1.0-SNAPSHOT"

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