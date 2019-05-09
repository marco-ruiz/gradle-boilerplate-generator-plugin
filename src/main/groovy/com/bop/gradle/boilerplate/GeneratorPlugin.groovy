/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bop.gradle.boilerplate

import java.nio.file.Paths

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement

/**
 * @author Marco Ruiz
 */
class GeneratorPlugin implements Plugin<Project> {
	
	private static final String PLUGIN_ROOT_URL = GeneratorPlugin.protectionDomain.codesource.location.toExternalForm()
	private static final String BOILERPLATES_ROOT = "boilerplates/"
	
	void apply(Project project) {
		project.configure(project) {
			//			extensions.create("boilerplates", BoilerplateGeneratorExtension, boilerplates)
		}
		
		FileTree pluginBundle = project.zipTree(PLUGIN_ROOT_URL)

		Set boilerplatesAvailable = []
		pluginBundle.visit { 
			if (isBoilerplateDirectory(it)) 
				boilerplatesAvailable << it.relativePath.segments[1] 
		}

		boilerplatesAvailable.each { String boilerplateName -> 
			project.task("boilerplate${boilerplateName.capitalize()}", type: GenerateBoilerplateTask) {
				boilerplateBundle = pluginBundle
				boilerplateSubDir = getStandardBoilerplateSubDir("${boilerplateName}/")

				projectDataModel.project = project
				projectDataModel.srcDir = project.sourceSets.main.java.srcDirs.first().toString()
			}
		}
	}
	
	boolean isBoilerplateDirectory(FileTreeElement ele) {
		ele.directory && 
		ele.relativePath.toString().startsWith("${BOILERPLATES_ROOT}") && 
		ele.relativePath.getSegments().length == 2
	}
	
	String getStandardBoilerplateSubDir(String name) {
		"${BOILERPLATES_ROOT}${name}"
	}
}
