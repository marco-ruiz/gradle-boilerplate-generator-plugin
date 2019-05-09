buildscript {
	repositories {
		mavenLocal()
		jcenter()
		mavenCentral()
		maven { url "https://plugins.gradle.org/m2/" }
	}
}

plugins {
	id 'groovy'
	id 'java-gradle-plugin'
}

repositories {
	mavenLocal()
	jcenter()
	mavenCentral()
}

dependencies {
	implementation localGroovy()
}

gradlePlugin {
	plugins {
		thePlugin {
			id = "${r"${project.group}.${project.name}"}"
			implementationClass = 'com.bop.gradle.boilerplate.GeneratorPlugin'
		}
	}
}

wrapper {
	description "Regenerates the Gradle Wrapper files"
	gradleVersion = '5.4'
	distributionUrl = "http://services.gradle.org/distributions/gradle-${r"${gradleVersion}"}-all.zip"
}
