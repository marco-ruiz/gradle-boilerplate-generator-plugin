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

import freemarker.cache.StringTemplateLoader
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler

/**
 * @author Marco Ruiz
 */
class FreeMarkerTranslator {
	
	Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
	
	FreeMarkerTranslator(Map<String, String> mappings) {
		if (mappings) {
			StringTemplateLoader templateLoader = new StringTemplateLoader();
			mappings.each { key, value ->  templateLoader.putTemplate(key, value ?: '')}
			cfg.setTemplateLoader(templateLoader)
		}
		
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
	}
	
	String translateTemplateString(Map dataModel, String templateString) {
		translate(dataModel, new Template("name", new StringReader(templateString), cfg));
	}

	String translateTemplateFile(Map dataModel, String templateFilename) {
		translate(dataModel, cfg.getTemplate(templateFilename));
	}

	String translate(Map dataModel, Template template) {
		StringWriter out = new StringWriter()
		template.process(dataModel, out)
		out.toString()
	}
}
