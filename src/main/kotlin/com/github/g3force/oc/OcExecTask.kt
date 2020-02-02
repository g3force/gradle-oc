package com.github.g3force.oc

import org.apache.commons.io.IOUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

abstract class OcExecTask : DefaultTask() {

    private val logger: Logger = LoggerFactory.getLogger(OcExecTask::class.java)

    @Input
    var args: List<String> = emptyList()

    @Input
    var showOutput: Boolean = false

    @TaskAction
    open fun doAction() {
        execute(args)
    }

    fun execute(arguments: List<String>) {
        val command: MutableList<String> = mutableListOf(findOcBinary())
        command.addAll(arguments)

        logger.info("Executing: ${command.joinToString(" ")}")

        val pb = ProcessBuilder(command)
                .directory(File("."))
                .redirectErrorStream(true)
        pb.environment()["KUBECONFIG"] = "build/kube.config"
        val proc = pb.start()
        val errorCode = proc.waitFor()

        val reader = BufferedReader(InputStreamReader(proc.inputStream))
        val output = IOUtils.readLines(reader).joinToString("\n")

        logger.info("Output: \n${output}")

        if (showOutput) {
            println(command.joinToString(" "))
            println(output)
        }

        if (errorCode != 0) {
            throw GradleException("Non zero error code '$errorCode' for command ${pb.command()}:\n${output}")
        }
    }

    private fun findOcBinary(): String {
        val config = project.extensions.getByType(OcPluginExtension::class.java)
        val configuredBinary = config.ocBinary
        if (configuredBinary?.exists() == true) {
            return configuredBinary.absolutePath
        }
        return ExecutableFinder().find("oc") ?: throw GradleException("No oc binary found")
    }
}