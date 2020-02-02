plugins {
    id("com.github.g3force.oc")
}

oc {
    // Set the path to the oc binary manually or let it be detected from your path
//    ocBinary = File("${System.getenv("user.home")}/.local/bin/oc")
    clusterUrl = "https://your.openshift.cluster:443"
    projectName = "your-project"
    // Set either a token file or the token itself. The tokenFile takes precedence, if it exists
    tokenFile = File("service-account.token")
//    token = "secret-token"
}

// Apply all .yaml files in the templates directory
tasks.register<com.github.g3force.oc.OcApplyTask>("ocApply") {
    source = fileTree("${projectDir}/templates") {
        include("*.yaml")
    }
}
