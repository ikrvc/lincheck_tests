plugins {
    kotlin("jvm") version "1.9.22"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlinx:lincheck:2.28")
    testImplementation("junit:junit:4.13")
}

kotlin {
    jvmToolchain(19)
}

tasks.withType<Test> {
    jvmArgs( "--add-opens", "java.base/java.lang=ALL-UNNAMED", "--add-opens", "java.base/jdk.internal.vm=ALL-UNNAMED", "--add-opens", "java.base/jdk.internal.access=ALL-UNNAMED", "--add-opens", "java.base/jdk.internal.misc=ALL-UNNAMED", "--add-exports", "java.base/jdk.internal.util=ALL-UNNAMED", "--add-exports", "java.base/sun.security.action=ALL-UNNAMED" )
    maxHeapSize = "16g"
}

