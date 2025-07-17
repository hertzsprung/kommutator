plugins {
    kotlin("jvm") version "2.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.aws.sdk.dynamodb)
    testRuntimeOnly(libs.slf4j.simple)
}

defaultTasks("check")