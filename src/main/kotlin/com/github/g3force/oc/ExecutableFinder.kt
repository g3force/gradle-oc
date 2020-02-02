package com.github.g3force.oc

import org.apache.commons.lang3.SystemUtils
import java.io.File

class ExecutableFinder {

    var pathSource = { System.getenv("PATH") }

    /**
     * Find the executable by scanning the file system and the PATH.
     * For windows, the ".exe" ending will be appended to the name.
     *
     * @param named The name of the executable to find
     * @return The absolute path to the executable, or null if none was found
     */
    fun find(named: String): String? {
        val osDepName = getOsDependentName(named)
        val allPaths = listOf("") + getPaths()
        for (pathSegment in allPaths) {
            val file = File(pathSegment, osDepName)
            if (canExecute(file)) {
                return file.absolutePath
            }
        }
        return null
    }

    private fun getOsDependentName(named: String) = if (SystemUtils.IS_OS_WINDOWS) "$named.exe" else named

    private fun getPaths() = pathSource()?.split(File.pathSeparator) ?: emptyList()

    private fun canExecute(file: File) = file.exists() && !file.isDirectory && file.canExecute()
}