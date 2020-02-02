package com.github.g3force.oc;

import org.gradle.api.Plugin
import org.gradle.api.Project

class OcPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create<OcPluginExtension>("oc", OcPluginExtension::class.java)

        val ocLoginTask = project.tasks.register("ocLogin", OcLoginTask::class.java).get()
        val ocProjectTask = project.tasks.register("ocProject", OcProjectTask::class.java).get()
        ocProjectTask.dependsOn(ocLoginTask)
    }
}