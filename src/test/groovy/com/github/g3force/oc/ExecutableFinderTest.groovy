package com.github.g3force.oc


import org.gradle.internal.impldep.org.apache.commons.lang.SystemUtils
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

class ExecutableFinderTest extends Specification {

    @Subject
    private ExecutableFinder executableFinder = new ExecutableFinder()

    @Rule
    private TemporaryFolder tempFolder

    private File pathOne
    private File pathTwo

    def setup() {
        pathOne = tempFolder.newFolder("one")
        pathTwo = tempFolder.newFolder("two")
        executableFinder.pathSource = { "${pathOne.absolutePath}${File.pathSeparator}${pathTwo.absolutePath}".toString() }
    }

    def "Find in path one"() {
        when:
        def ocBinary = createBinary(pathOne)

        then:
        def result = executableFinder.find("oc")
        result != null
        result.startsWith(ocBinary.absolutePath)
    }

    def "Find in path two"() {
        when:
        def ocBinary = createBinary(pathTwo)

        then:
        def result = executableFinder.find("oc")
        result != null
        result.startsWith(ocBinary.absolutePath)
    }

    def "Do not find if not present"() {
        when:
        true

        then:
        def result = executableFinder.find("oc")
        result == null
    }

    def createBinary(File folder) {
        def ocBinary = new File(folder, "oc" + (SystemUtils.IS_OS_WINDOWS ? ".exe" : ""))
        assert ocBinary.createNewFile()
        assert ocBinary.setExecutable(true, false)
        ocBinary
    }
}
