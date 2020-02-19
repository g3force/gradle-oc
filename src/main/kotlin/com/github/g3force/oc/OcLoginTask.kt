package com.github.g3force.oc

import org.gradle.api.tasks.TaskAction


abstract class OcLoginTask : OcExecTask() {

    @TaskAction
    override fun doAction() {
        val config = project.extensions.getByType(OcPluginExtension::class.java)
        val tokenFile = config.tokenFile
        if (tokenFile != null && tokenFile.exists()) {
            config.token = tokenFile.readText()
        }

        execute(listOf("login", config.clusterUrl, "--token", config.token))
    }
}