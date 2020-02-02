![CI](https://github.com/g3force/gradle-oc/workflows/CI/badge.svg)

# Gradle OC Plugin

This gradle plugin is a wrapper around a (currently only local) OC binary.
You can specify the login configuration, apply yaml templates and create custom oc tasks.

## Usage

Include the plugin in your build:
```kotlin
plugins {
    id("com.github.g3force.oc") version "0.2.0"
}
```

And configure it:
```kotlin
oc {
    // Set the path to the oc binary manually or let it be detected from your path
//    ocBinary = File("${System.getenv("user.home")}/.local/bin/oc")
    clusterUrl = "https://your.openshift.cluster:443"
    projectName = "your-project"
    // Set either a token file or the token itself. The tokenFile takes precedence, if it exists
    tokenFile = File("service-account.token")
//    token = "secret-token"
}
```

To apply templates, define a `OcApplyTask` in your `build.gradle.kts`:
```kotlin
// Apply all .yaml files in the templates directory
tasks.register<com.github.g3force.oc.OcApplyTask>("ocApply") {
    source = fileTree("${projectDir}/templates") {
        include("*.yaml")
    }
}
```

You can also define your own custom tasks:
```kotlin
tasks.register<com.github.g3force.oc.OcExecTask>("ocGetMyService") {
    args = listOf("get", "service", "my-service")
    showOutput = true
    dependsOn(tasks.findByPath(":ocProject"))
}
```

Also take the [example project](./example) as a reference.
