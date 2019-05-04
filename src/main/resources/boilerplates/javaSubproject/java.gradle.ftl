buildscript {
	repositories {
		mavenLocal()
		jcenter()
		gradlePluginPortal()
		mavenCentral()
		maven { url 'https://plugins.gradle.org/m2/' }
		maven { url 'https://repo.spring.io/libs-snapshot' }
		maven { url 'https://repository.jboss.org/nexus/content/repositories/releases' }
	}
}

apply plugin: java
apply plugin: 'eclipse'
eclipse.classpath.downloadJavadoc = true

sourceCompatibility = 1.8
targetCompatibility = 1.8
build.mustRunAfter clean

repositories {
	jcenter()
	mavenLocal()
	mavenCentral()
	maven { url 'https://repo.spring.io/libs-snapshot' }
	maven { url 'https://repository.jboss.org/nexus/content/repositories/releases' }
}

dependencies {
	testImplementation 'junit:junit:4.12'
	testImplementation "org.hamcrest:hamcrest-core:1.3"
	testImplementation "org.hamcrest:hamcrest-library:1.3"
}

task stage(dependsOn: ['build', 'clean'])

defaultTasks 'build'

wrapper {
	description "Regenerates the Gradle Wrapper files"
	gradleVersion = '5.4'
	distributionUrl = "http://services.gradle.org/distributions/gradle-${gradleVersion}-all.zip"
}
