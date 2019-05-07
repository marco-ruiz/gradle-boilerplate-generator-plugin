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

/**
 * @author Marco Ruiz
 */
class FileGenerator {
	
	private static final FreeMarkerTranslator TRANSLATOR_FILENAMES = new FreeMarkerTranslator()

	FileDescriptor fileDescriptor
	Map dataModel
	File outputFile
	
	FileGenerator(FileDescriptor fileDescriptor, Map generatorDataModel) {
		this.fileDescriptor = fileDescriptor
		dataModel = generatorDataModel.clone()
		outputFile = resolveFile()
		
		// Augment data model with newly resolved outputFile properties (not available when resolving it)
		dataModel.outputFile = outputFile
		if (fileDescriptor.isSource())
			addClassDetailsToDataModel(outputFile.toString() - dataModel.srcDir)
	}
	
	void addClassDetailsToDataModel(String classFilePath) {
		String className = ''
		String[] packageBranches = FileDescriptor.getPathBranches(classFilePath, File.separator)
		if (!packageBranches[0]) packageBranches = packageBranches[(1..-1)]
		
		if (!fileDescriptor.isDirectory()) {
			String classFileName = packageBranches[-1]
			className = classFileName[0..<classFileName.lastIndexOf('.')]
			packageBranches = packageBranches[(0..-2)]
		}
		
		dataModel.fullPackage = packageBranches.join('.')
		dataModel.className = className
	}
	
	String getRequirementsError() {
		String id = outputFile
		if (outputFile.getParentFile().isFile())
			return "Parent of ${id} exists and is a file; not a directory"
			
		if (fileDescriptor.reusable)
			return "" // Disposable
			
		if (!outputFile.exists())
			return "" // Fully generate-able
		
		// At this point file exists and it is not reusable (so must be generated!)

		// Check override violation
		if (!fileDescriptor.snippet && !fileDescriptor.isDirectory())
			return "${id} exists and is not optional, nor a snippet, nor a directory"

		// Check directory/file mismatch
		if (outputFile.isDirectory() && !fileDescriptor.isDirectory())
			return "${id} exists and it is not a file; but a directory and ${id} is a file to generate"
		
		if (!outputFile.isDirectory() && fileDescriptor.isDirectory())
			return "${id} exists and it is not a directory; but a file and ${id} is a directory to generate"

		return ""
	}
	
	File resolveFile() {
		File rootFile = fileDescriptor.source ? dataModel.srcDir : dataModel.project.projectDir
		String translatedOutputPath = TRANSLATOR_FILENAMES.translateTemplateString(dataModel, fileDescriptor.outputPath)
		String[] branches = fileDescriptor.getTranslatedOutputPathBranches(translatedOutputPath)
		Paths.get(rootFile.toString(), branches).toFile()
	}
	
	void generateFile(FreeMarkerTranslator translatorFiles) {
		if (fileDescriptor.isDirectory())
			outputFile.mkdirs()
		else {
			outputFile.getParentFile().mkdirs()
			String content = translatorFiles.translateTemplateFile(dataModel, fileDescriptor.templatePath)
			if (!outputFile.exists()) {
				outputFile.text = content
			} else if (fileDescriptor.snippet) {
				outputFile.text += content
			}
		}
	}
/*	
	String evaluate(String text) {
		text.replaceAll(/\$\{(.+?)\}/) { match -> binding[match[1]] ?: match[0] }
	}
*/
}
