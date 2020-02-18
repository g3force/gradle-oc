package com.github.g3force.oc

import org.gradle.api.file.FileTree
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class OcApplyTask : OcExecTask() {

    @get:InputFiles
    abstract var source: FileTree

    @TaskAction
    override fun doAction() {
        source.forEach { applyFile(it) }
    }

    private fun applyFile(file: File) {
        execute(listOf("apply", "-f", file.absolutePath))
    }
}