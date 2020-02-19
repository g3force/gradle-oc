package com.github.g3force.oc

import org.apache.commons.io.IOUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

abstract class OcExecTask : DefaultTask() {
    @Input
    var args: List<String> = ArrayList()

    @TaskAction
    open fun doAction() {
        execute(emptyList())
    }

    fun execute(baseCommand: List<String>) {
        val config = project.extensions.getByType(OcPluginExtension::class.java)
        val binary = findOcBinary(config.ocBinary)

        val command: MutableList<String> = mutableListOf(binary)
        if (config.insecure) {
            command.add("--insecure-skip-tls-verify")
        }

        command.addAll(baseCommand)
        command.addAll(args)

        logger.info("Executing: ${command.joinToString(" ")}")

        val processBuilder = ProcessBuilder(command)
                .directory(File("."))
                .redirectErrorStream(true)
        processBuilder.environment()["KUBECONFIG"] = "build/kube.config"
        val process = processBuilder.start()
        val errorCode = process.waitFor()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = IOUtils.readLines(reader).joinToString("\n")

        logger.info("Output: \n${output}")

        if (errorCode != 0) {
            throw GradleException("Non zero error code '$errorCode' for command ${processBuilder.command()}:\n${output}")
        }
    }

    private fun findOcBinary(configuredBinary: File?): String {
        if (configuredBinary?.exists() == true) {
            return configuredBinary.absolutePath
        }
        return ExecutableFinder().find("oc") ?: throw GradleException("No oc binary found")
    }
}