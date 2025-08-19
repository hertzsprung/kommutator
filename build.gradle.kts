plugins {
    kotlin("jvm") version "2.2.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.aws.sdk.dynamodb)
    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.assertions.core)
    testRuntimeOnly(libs.slf4j.simple)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

defaultTasks("check")