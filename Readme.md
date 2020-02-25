![CI](https://github.com/g3force/gradle-oc/workflows/CI/badge.svg)

# Gradle OC Plugin

This gradle plugin is a wrapper around a (currently only local) OC binary.
You can specify the login configuration, apply yaml templates and create custom oc tasks.

## Usage

Find instruction on how to include the plugin in your build on the Gradle plugin portal: https://plugins.gradle.org/plugin/com.github.g3force.oc

Then configure it:
```kotlin
oc {
    // Set the path to the oc binary manually or let it be detected from your path
    // ocBinary = File("${System.getenv("user.home")}/.local/bin/oc")
    clusterUrl = "https://your.openshift.cluster:443"
    projectName = "your-project"
    // Set either a token file or the token itself. The tokenFile takes precedence, if it exists
    tokenFile = File("service-account.token")
    // token = "secret-token"
    // To login via plain text credentials
    credentials {
        username = "admin"
        password = "admin"
        //
        // you can provide them via Gradle project properties:
        //      * /home/user/.gradle/gradle.properties
        //      * project/gradle.properties
        //      * -Pproperty
        //username = project.findProperty("ocUsername")
        //password = project.findProperty("ocPassword")
        //
        // or via System environment
        //username = System.getenv("OC_USERNAME")
        //password = System.getenv("OC_PASSWORD")
    }
    // Disable TLS verify in case you are connecting to a dev cluster with self-signed certificate
    insecure = false
}
```

#### Authorisation
OC login would look for the auth data in the following order:
* From `token` configuration property if present
* From `tokenFile` configuration property if present
* From `credentials` configuration property if present
* No auth data in login request

To apply templates, define a `OcApplyTask` in your `build.gradle.kts`:
```kotlin
// Apply all .yaml files in the templates directory
tasks.register<com.github.g3force.oc.OcApplyTask>("ocApply") {
    source = fileTree("${projectDir}/templates") {
        include("*.yaml")
    }
}
```
Or pass any additional CLI arguments:
```kotlin
// Apply all .yaml files in the templates directory without overwriting resources in cluster
tasks.register<com.github.g3force.oc.OcApplyTask>("ocApply") {
    args = listOf("--overwrite=false")
    source = fileTree("${projectDir}/templates") {
        include("*.yaml")
    }
}
```
You can also define your own custom tasks:
```kotlin
tasks.register<com.github.g3force.oc.OcExecTask>("ocGetMyService") {
    args = listOf("get", "service", "my-service")
    dependsOn(tasks.findByPath(":ocProject"))
}
```

Also, take the [example project](./example) as a reference.
