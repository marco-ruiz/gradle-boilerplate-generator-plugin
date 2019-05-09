package ${fullPackage}

import org.gradle.api.Plugin
import org.gradle.api.Project

class ${className} implements Plugin<Project> {
	
	void apply(Project project) {
		applyExtension(project)
		applyTasks(project)
	}
	
	void applyExtension(Project project) {
		project.configure(project) {
			extensions.create("${extensionName}", ${className}Extension)
		}
	}	
	
	void applyTasks(Project project) {
		project.task("run${className}Task", type: ${className}Task) {
		
		}
	}
}
