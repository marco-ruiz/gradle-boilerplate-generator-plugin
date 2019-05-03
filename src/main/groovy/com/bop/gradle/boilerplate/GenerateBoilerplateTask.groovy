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

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class GenerateBoilerplateTask extends DefaultTask {

	private static String CONFIG_PATH = "META-INF/boilerplate.yaml"
	
	@Input FileTree boilerplateBundle
	@Input String boilerplateSubDir = ''
	@Input File projectDir, srcDir
	@Input String rootPackage
	
	GenerateBoilerplateTask() {
		group = 'Boilerplate Generation'
		description = 'Generates the respective boilerplate files and directory structure'
		if (boilerplateSubDir.length() > 0 && boilerplateSubDir[-1] != "/") 
			boilerplateSubDir += "/"
	}
	
	@TaskAction
	void generateAction() {
		BoilerplateConfig config = BoilerplateConfig.read(findBundleFile(CONFIG_PATH))
		List<TranslationGenerator> generators = config.translations.collect { 
			new TranslationGenerator(it, config.bindings.clone()) 
		}
		
		// Abort if override error
		List<String> errors = generators.collect { it.requirementsError }
		boolean failed = errors.find { !it.isEmpty() }
		
		if (failed) {
			println "Unable to satisfy requirements to generate boilerplate:"
			errors.eachWithIndex { errorMsg, idx -> 
				if (!errorMsg.isEmpty()) println "[${idx + 1}]: ${errorMsg}" 
			}
		} else
			generators.each { it.generate() }
	}
	
	File findBundleFile(String fileSubpath) {
		return boilerplateBundle.matching { include "${boilerplateSubDir}**/${fileSubpath}" }.files.first()
	}
		
	class TranslationGenerator {
		TranslationConfig translation
		Map binding
		File outputFile
		
		TranslationGenerator(TranslationConfig translation, Map binding) {
			this.translation = translation
			this.binding = binding
			appendTranslatorBindings()
			
			outputFile = (translation.isSourceFile()) ?
				resolveOutputFile(srcDir, binding.fullPackage.split('\\.').toList()) :
				resolveOutputFile(projectDir, [])
		}
		
		void appendTranslatorBindings() {
			binding.srcDir = srcDir.toString()
			binding.projectDir = projectDir.toString()
			binding.rootPackage = rootPackage
	
			binding.filename = translation.filename
			binding.subDir = translation.subDir
			binding.subPackage = translation.subPackage
			
			binding.fullPackage = "${rootPackage}.${binding.subPackage}"
		}
		
		File resolveOutputFile(File parentDir, List<String> subDirsList) {
			List<String> branches = translation.subDirBranches + subDirsList + binding.filename
			String[] subDirs = branches.collect { evaluate(it) }.toArray(new String[0])
			Paths.get(parentDir.toString(), subDirs).toFile()
		}
		
		String getRequirementsError() {
			String id = outputFile
			if (!translation.required) 
				return "" // Disposable
				
			if (outputFile.getParentFile().isFile()) 
				return "Parent of ${id} exists and is a file; not a directory"
				
			if (!outputFile.exists()) 
				return "" // Fully generate-able
			
			// At this point file exists and it is required
			if (!translation.appendixSnippet) 
				return "${id} exists and is not optional, nor a snippet"

			// Check directory/file mismatch				
			if (outputFile.isDirectory() && !translation.isDir())
				return "${id} exists and it is not a file; but a directory and ${id} is a file to generate"
			
			if (!outputFile.isDirectory() && translation.isDir())
				return "${id} exists and it is not a directory; but a file and ${id} is a directory to generate"
				
			return ""
		}

		void generate() {
			if (translation.isDir())
				outputFile.mkdirs()
			else {
				outputFile.getParentFile().mkdirs()
				String newContent = evaluate(findBundleFile(translation.templateFilename).text)
				if (!outputFile.exists()) {
					outputFile.text = newContent 
				} else if (translation.appendixSnippet) {
					outputFile.text += newContent
				}
			}
		}
		
		String evaluate(String text) {
			text.replaceAll(/\$\{(.+?)\}/) { match -> binding[match[1]] ?: match[0] }
		}
	}
}

