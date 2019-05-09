package ${fullPackage}

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class ${className} extends DefaultTask {

	${className}() {
		group = ''
		description = ''
	}
	
	@TaskAction
	void taskAction() {
		def ${extensionName} = project.${extensionName}
	}
}

