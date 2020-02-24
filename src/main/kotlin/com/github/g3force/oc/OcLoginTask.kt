package com.github.g3force.oc

import org.gradle.api.tasks.TaskAction


abstract class OcLoginTask : OcExecTask() {

    @TaskAction
    override fun doAction() {
        val config = project.extensions.getByType(OcPluginExtension::class.java)
        val loginArgs = mutableListOf("login", config.clusterUrl)
        loginArgs.addAll(getLoginArgs(config))
        execute(loginArgs)
    }

    private fun getLoginArgs(config: OcPluginExtension): List<String> {
        val tokenFile = config.tokenFile
        config.token?.let {
            return listOf("--token", it)
        }
        if (tokenFile != null && tokenFile.exists()) {
            return listOf("--token", tokenFile.readText())
        }
        val credentials = config.credentials
        if (!credentials.isEmpty()) {
            return listOf("-u", credentials.username, "-p", credentials.password)
        }
        logger.info("No authorisation data provided")
        return emptyList()
    }
}