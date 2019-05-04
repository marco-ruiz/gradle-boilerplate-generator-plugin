buildscript {
    dependencies {
        classpath 'com.moowork.gradle:gradle-node-plugin:1.2.0'
    }
}

apply plugin: 'base'
apply plugin: 'com.moowork.node' // gradle-node-plugin

node {
    /* gradle-node-plugin configuration
       https://github.com/srs/gradle-node-plugin/blob/master/docs/node.md

       Task name pattern:
       ./gradlew npm_<command> Executes an NPM command.
    */

    // Version of node to use.
    version = '10.14.1'

    // Version of npm to use.
    npmVersion = '6.4.1'

    // If true, it will download node using above parameters.
    // If false, it will try to use globally installed node.
    download = true
}

nodeSetup.onlyIf {
	!nodeSetup.nodeDir.exists()
}

npmSetup.onlyIf {
	!npmSetup.npmDir.exists()
}

npm_run_build {
    // make sure the build task is executed only when appropriate files change
    inputs.files fileTree('public')
    inputs.files fileTree('src')

    // 'node_modules' appeared not reliable for dependency change detection (the task was rerun without changes)
    // though 'package.json' and 'package-lock.json' should be enough anyway
    inputs.file 'package.json'
    inputs.file 'package-lock.json'

    outputs.dir 'build'
	
	environment = [
		'CI': 'false'
	]
}

// pack output of the build into JAR file
task packageNpmApp(type: Zip, dependsOn: npm_run_build) {
	inputs.files fileTree('build')
    baseName 'npm-app'
    extension 'jar'
    destinationDir file("${projectDir}/build_packageNpmApp")
    from('build') {
        // optional path under which output will be visible in Java classpath, e.g. static resources path
        into 'static'
    }
}

// declare a dedicated scope for publishing the packaged JAR
configurations {
    npmResources
}

configurations.default.extendsFrom(configurations.npmResources)

// expose the artifact created by the packaging task
artifacts {
    npmResources(packageNpmApp.archivePath) {
        builtBy packageNpmApp
        type 'jar'
    }
}

assemble.dependsOn packageNpmApp
/*
String testsExecutedMarkerName = "${projectDir}/.tests.executed"

task test(type: NpmTask) {
    dependsOn assemble

    // force Jest test runner to execute tests once and finish the process instead of starting watch mode
    environment CI: 'true'

    args = ['run', 'test']
    
    inputs.files fileTree('src')
    inputs.file 'package.json'
    inputs.file 'package-lock.json'

    // allows easy triggering re-tests
    doLast {
        new File(testsExecutedMarkerName).text = 'delete this file to force re-execution JavaScript tests'
    }
    outputs.file testsExecutedMarkerName
}

check.dependsOn test
*/
clean {
    delete packageNpmApp.archivePath
//    delete testsExecutedMarkerName
}
