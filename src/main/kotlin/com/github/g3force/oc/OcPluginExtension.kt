package com.github.g3force.oc

import org.gradle.api.Action
import java.io.File

open class OcPluginExtension {
    var ocBinary: File? = null
    var clusterUrl: String = ""
    var projectName: String = ""
    var token: String? = null
    var tokenFile: File? = null
    var credentials: Credentials = Credentials()
    var insecure = false

    fun credentials(action: Action<Credentials>) {
        action.execute(credentials)
    }
}

open class Credentials {
    var username: String = ""
    var password: String = ""

    fun isEmpty(): Boolean {
        return username.isBlank() || password.isBlank()
    }
}