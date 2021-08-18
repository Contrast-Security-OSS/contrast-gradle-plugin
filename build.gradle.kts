plugins {
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation(gradleApi())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

gradlePlugin {
    val contrast by plugins.creating {
        id = "com.contrastsecurity.base"
        implementationClass = "com.contrastsecurity.gradle.ContrastBasePlugin"
    }
}

tasks.compileJava {
    options.release.set(8)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//region Functional Tests
val functionalTestSourceSet = sourceSets.create("functionalTest")

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
    dependsOn(functionalTest)
}
//endregion
