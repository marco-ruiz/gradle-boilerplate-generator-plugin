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
import java.util.regex.Pattern

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.org.bouncycastle.util.encoders.Translator

/**
 * @author Marco Ruiz
 */
class GenerateBoilerplateTask extends DefaultTask {

	private static final String BUNDLE_CONFIG_PATH = "META-INF/boilerplate.yaml"
	
	@Input FileTree boilerplateBundle
	@Input String boilerplateSubDir = ''
	@Input Map taskDataModel = [:]
	BoilerplateConfig config
	
	GenerateBoilerplateTask() {
		group = 'Boilerplate Generation'
		description = 'Generates the respective boilerplate files and directory structure'
		if (boilerplateSubDir.length() > 0 && boilerplateSubDir[-1] != "/") 
			boilerplateSubDir += "/"
	}
	
	@TaskAction
	void generateAction() {
		config = BoilerplateConfig.read(findBundleFile(BUNDLE_CONFIG_PATH))
		
		Map generatorDataModel = createGeneratorDataModel()
		List<FileGenerator> generators = config.fileOutputs.all.collect { 
			new FileGenerator(it, generatorDataModel) 
		}
		
		// Abort if override error
		if (!meetRequirements(generators)) 
			return
			
		Map<String, String> templateMappings = generators
				.collect { it.fileDescriptor.templatePath }
				.findAll { it }
				.collectEntries { [ (it) : findBundleFile(it)?.text] }
		
		FreeMarkerTranslator translatorFiles = new FreeMarkerTranslator(templateMappings)
		generators.each { it.generateFile(translatorFiles) }
	}
	
	boolean meetRequirements(List<FileGenerator> generators) {
		List<String> errors = generators.collect { it.requirementsError }
		boolean failed = errors.find { !it.isEmpty() }
		
		if (failed) {
			logger.error "Unable to satisfy requirements to generate boilerplate:"
			errors.eachWithIndex { errorMsg, idx ->
				if (!errorMsg.isEmpty()) 
					logger.error "[${idx + 1}]: ${errorMsg}"
			}
		}
		return !failed
	}
	
	Map createGeneratorDataModel() {
		FreeMarkerTranslator translator = new FreeMarkerTranslator()
		Map result = [:] 
		config.dataModel.each { key, value -> 
			result[key] = translator.translateTemplateString(taskDataModel, value) 
		}
		result += taskDataModel
		if (!result.srcDir) 
			result.srcDir = result.project.sourceSets.main.java.srcDirs.first()
			
		result.srcDir = new File(result.srcDir.toString())
		return result
	}
	
	File findBundleFile(String fileSubpath) {
		Set<File> matchingFiles = boilerplateBundle.matching { include "${boilerplateSubDir}**/${fileSubpath}" }.files
		return matchingFiles ? matchingFiles.first() : null
	}
}

