plugins {
    `java-gradle-plugin`
    groovy
    kotlin("jvm") version "1.3.61"
    id("com.gradle.plugin-publish") version "0.10.1"
}

group = "com.github.g3force"
version = "0.1.0"

repositories {
    jcenter()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib"))
    implementation("commons-io:commons-io:2.6")
    implementation("org.apache.commons:commons-lang3:3.9")

    testImplementation("org.codehaus.groovy:groovy-all:2.5.8")
    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
}

gradlePlugin {
    plugins {
        create("ocPlugin") {
            id = "com.github.g3force.oc"
            displayName = "OpenShift oc wrapper plugin"
            description = "Use the OpenShift oc client from within Gradle"
            implementationClass = "com.github.g3force.oc.OcPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/g3force/gradle-oc"
    vcsUrl = "https://github.com/g3force/gradle-oc.git"
    tags = listOf("openshift", "oc", "ci/cd")
}
