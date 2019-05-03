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

import java.nio.file.Path
import java.nio.file.Paths

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import groovy.transform.ToString

/**
 * @author Marco Ruiz
 */
@ToString
class BoilerplateConfig {

	Map bindings
	List<TranslationConfig> translations
	
	static BoilerplateConfig read(File yamlFile) {
		InputStream input = new FileInputStream(yamlFile);
		Yaml yaml = new Yaml(new Constructor(BoilerplateConfig))
		yaml.loadAs(input, BoilerplateConfig)
	}
}

@ToString
class TranslationConfig {
	
	private static final String TRANSLATION_TYPE_SOURCE = "source"
	private static final String TRANSLATION_TYPE_PROJECT = "project"
	
	String templateFilename
	String type
	String filename
	String subDir = ""
	String subPackage
	boolean required = true
	boolean appendixSnippet = false

	boolean isDir() {
		templateFilename == null || templateFilename.isEmpty()
	}
	
	boolean isSourceFile() {
		type == TRANSLATION_TYPE_SOURCE
	}
	
	boolean isGeneratable() {
		dir || !required || appendixSnippet
	}
	
	List<String> getSubDirBranches() {
		Path all = Paths.get(subDir)
		int numBranches = all.getNameCount()
		numBranches < 2 ? [subDir] : (0..<numBranches).collect { all.getName(it).toString() }
	}
}
