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

import java.util.regex.Pattern

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import groovy.transform.ToString

/**
 * @author Marco Ruiz
 */
@ToString
class BoilerplateConfig {

	Map dataModel
	FileOutputs fileOutputs
	
	static BoilerplateConfig read(File yamlFile) {
		InputStream input = new FileInputStream(yamlFile);
		Yaml yaml = new Yaml(new Constructor(BoilerplateConfig))
		BoilerplateConfig result = yaml.loadAs(input, BoilerplateConfig)
		result.fileOutputs.postConstructSetup()
		return result
	}
}

class FileOutputs {
	List<FileDescriptor> files = []
	List<ClassDescriptor> classes = []
	List<FileDescriptor> directories = [], packages = []
	
	void postConstructSetup() {
		files.each 			{ it.postConstructSetup(false, false) }
		directories.each 	{ it.postConstructSetup(false, true) }
		classes.each 		{ it.postConstructSetup(true, false) }
		packages.each 		{ it.postConstructSetup(true, true) }
	}
	
	List<FileDescriptor> getAll() {
		(directories + packages + files + classes).findAll { it }
	}
}

@ToString
class FileDescriptor {

	private static final String PACKAGE_SEPARATOR = '.';
	private static final String FILE_PATH_SEPARATOR = '/';
	private static final String DEFAULT_PACKAGE = 'defaultPackage'

	static String[] getPathBranches(String childPath, String pathSeparator = FILE_PATH_SEPARATOR) {
		getPathBranches(childPath, false)
	}
	
	static String[] getPathBranches(String childPath, boolean isSource) {
		String[] result = childPath.split(Pattern.quote(isSource ? PACKAGE_SEPARATOR : FILE_PATH_SEPARATOR))
		return (isSource && result.length < 2) ? [DEFAULT_PACKAGE , *result] : result 
	}

	boolean source, directory
	String templatePath, outputPath
	boolean reusable = false
	boolean snippet = false
	String outputExtension = ''
	
	void postConstructSetup(boolean isSource, boolean isDirectory) {
		source = isSource
		directory = isDirectory
		if (outputExtension && !outputExtension.startsWith('.'))
			outputExtension = '.' + outputExtension
	}
	
	String[] getTemplatePathBranches() {
		getPathBranches(templatePath)
	}
	
	String[] getTranslatedOutputPathBranches(String translatedOutputPath) {
		String[] result = getPathBranches(translatedOutputPath, source)
		result[-1] += outputExtension
		return result
	}
}

class ClassDescriptor extends FileDescriptor {

	ClassDescriptor() { outputExtension = 'java' }
}

