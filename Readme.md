# Gradle OC Plugin

This gradle plugins is a wrapper around a (currently only local) OC binary.
You can specify the login configuration and apply yaml templates.

## Usage

Include the plugin in your build:
```kotlin
plugins {
    id("com.github.g3force.oc") version "0.1.0"
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

To apply templates, define a `OcApplyTask`:
```kotlin
// Apply all .yaml files in the templates directory
tasks.register<com.github.g3force.oc.OcApplyTask>("ocApply") {
    source = fileTree("${projectDir}/templates") {
        include("*.yaml")
    }
}
```

Also take the [example project](./example) as a reference.
