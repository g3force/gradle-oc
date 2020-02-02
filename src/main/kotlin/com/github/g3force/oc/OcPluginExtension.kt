package com.github.g3force.oc

import java.io.File


abstract class OcPluginExtension {
    var ocBinary: File? = null
    var clusterUrl: String = ""
    var projectName: String = ""
    var token: String = ""
    var tokenFile: File? = null
}