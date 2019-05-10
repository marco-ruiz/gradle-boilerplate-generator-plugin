# Gradle Boilerplate Generator Plugin [![Build Status](https://travis-ci.org/marco-ruiz/gradle-boilerplate-generator-plugin.svg?branch=master)](https://travis-ci.org/marco-ruiz/gradle-boilerplate-generator-plugin) [![Download](https://api.bintray.com/packages/marco-ruiz/mavenRepo/com.bop.gradle-boilerplate-generator-plugin/images/download.svg)](https://bintray.com/marco-ruiz/mavenRepo/com.bop.gradle-boilerplate-generator-plugin/_latestVersion) [![License](https://img.shields.io/github/license/marco-ruiz/gradle-boilerplate-generator-plugin.svg)](https://github.com/marco-ruiz/gradle-boilerplate-generator-plugin/blob/master/LICENSE)

## Overview

This gradle plugin allows you to generate customizable boilerplate files and directories. Each custom boilerplate is defined as a set of:
- Templates of files to generate
- Variables to substitute during generation (with optional default values)
- Files to generate (location and template to use)

Once such definition is avaiable to the plugin, the plugin can generate the appropriate files and directories by applying the user's data model to the boilerplate definition.

## Quick Start Guide

Create a new gradle project with the following `build.gradle` file:

```gradle
buildscript {
  repositories {
    jcenter()
    maven { url "https://dl.bintray.com/marco-ruiz/mavenRepo" }
  }
  dependencies {
    classpath 'com.bop:gradle-boilerplate-generator-plugin:1.0.2'
  }
}

apply plugin: 'java'
apply plugin: 'com.bop.gradle-boilerplate-generator-plugin'

wrapper {
  description "Regenerates the Gradle Wrapper files"
  gradleVersion = '5.4'
  distributionUrl = "http://services.gradle.org/distributions/gradle-${gradleVersion}-all.zip"
}

boilerplateWebServiceResource {
  taskDataModel = [
    srcDir: sourceSets.main.java.srcDirs.first(), 
    packagePrefix: 'com.my.microservice.',
    resourceName: 'MyGeneratedResource'
  ]
}
```

... and from the shell issue the command:

```
./gradlew boilerplateWebServiceResource
```

Now you can find under your `src/main/java` a set of classes typically needed to manage a web service resource through the different layers of abstraction (persistence, service, controller, data transfer, entity modelling, etc).

## Getting Started

Include the following in your build.gradle, to use this plugin:

```gradle
buildscript {
  repositories {
    jcenter()
    maven { url "https://dl.bintray.com/marco-ruiz/mavenRepo" }
  }
  dependencies {
    classpath 'com.bop:gradle-boilerplate-generator-plugin:1.0.2'
  }
}

apply plugin: 'com.bop.gradle-boilerplate-generator-plugin'
```

## Usage

Define a gradle task of type `com.bop.gradle.boilerplate.GenerateBoilerplateTask`, in your `build.gradle` file and preconfigure its `taskDataModel` map with the variable substitutions values desired to be used in the boilerplate, and that's it! The following is an example of this:

```gradle
task myBoilerplate(type: com.bop.gradle.boilerplate.GenerateBoilerplateTask) {
  boilerplateBundle = fileTree(${BUNDLE_LOCATION})
  taskDataModel = [:]
}
```

The plugin is extensible enough to allow the user to define its own boilerplates. The plugin comes with a set of pre-configured boilerplate descriptions to use out-of-the-box; each with a task associated with it, with the name `boilerplate${BOILERPLATE_NAME}`. So, alternatively, one of these tasks may be used to generate its corresponding boilerplates without the need to create their description. Their configuration takes the following form:

```gradle
boilerplate${BOILERPLATE_NAME} {
  taskDataModel = [:]
}
```

#### Custom Boilerplate Definition

The plugin reads a boilerplate defintion from a [file tree](https://docs.gradle.org/current/userguide/working_with_files.html#sec:file_trees) with the following structure:

```console
├── META-INF
│   └── boilerplate.yaml
├── Template1.ftl
├── Template2.ftl
├── ...
└── TemplateN.ftl
```

The `TemplateX.ftl` files are [FreeMarker](https://freemarker.apache.org/) template files to be used to generate the boilerplate file contents.

The `boilerplate.yaml` is the boilerplate descriptor (in [YAML](https://yaml.org/spec/1.1/current.html) format) and it lists:
1. The substitution variables used by this boilerplate (with default values) 
2. The files/directories to generate. 

This descriptor must have the following structure:

```yaml
dataModel: # Substitution variables
    VAR_1: "VALUE_1"
    VAR_2: "VALUE_2"
    # More variables
    VAR_N: "VALUE_N"

fileOutputs: # Files and directories to generate
    directories: # Directories to the created under the gradle's '${project.project.Dir}'. Sub paths separated by '/'
        - outputPath: "path1/relative/to/project/dir"
        - outputPath: "path2/relative/to/project/dir"
        # More 'outputPath's
        - outputPath: "pathN/relative/to/project/dir"
    
    # Packages to be created under the directory 'srcDir' specified in the 'taskDataModel'. Specified in dotted notation
    packages: 
        - outputPath: "package1.to.be.created.under.srcDir"
        - outputPath: "package2.to.be.created.under.srcDir"
        # More 'outputPath's
        - outputPath: "packageN.to.be.created.under.srcDir"
    
    files: # Files to the created under the gradle's '${project.project.Dir}'. Sub paths separated by '/'
        - templatePath: "TemplateX.ftl"
          outputPath: "path/relative/to/project/dir.extension"
          # If file to be generated already exists and it is not reusable the whole generation will abort
          reusable: true # Defaults to false. 
          # If file exists and it is snippet then it gets appended to the end of the existing file
          # If file exist and it is not snippet then the whole generation will abort
          snippet: true # Defaults to false.

        # More class descriptors
        
        - templatePath: "TemplateX.ftl"
          outputPath: "path/relative/to/project/dir.extension"
          
    # Classes to be created under the directory 'srcDir' specified in the 'taskDataModel'. Specified in dotted notation
    classes: 
        - templatePath: "TemplateX.ftl"
          outputPath: "fully.qualified.class.name"
          outputExtension: "groovy" # Defaults to 'java'
          # If file to be generated already exists and it is not reusable the whole generation will abort
          reusable: true # Defaults to false. 
          # If file exists and it is snippet then it gets appended to the end of the existing file
          # If file exist and it is not snippet then the whole generation will abort
          snippet: true # Defaults to false.
          
        # More class descriptors
        
        - templatePath: "TemplateY.ftl"
          outputPath: "fully.qualified.class.name"
```

The `templatePath`, `outputPath` and `outputExtension` properties of all the `fileOuputs` (`directories`, `packages`, `files`, `classes`) may reference substitution variables in the data model and `project` properties. For example, the following `boilerplate.yaml` file would create a subdirectory under the project directory with the name evaluated against the corresponding substitution variables:

```
dataModel:
    subProjectName: "common"
    subProjectPrefix: "${project.name}"

fileOutputs:
    directories:
        - outputPath: "${subProjectPrefix}-${subProjectName}"
```

For more example, check the [out-of-the-box boilerplates bundles](https://github.com/marco-ruiz/gradle-boilerplate-generator-plugin/tree/master/src/main/resources/boilerplates)

#### Out-of-the-box Boilerplates

The plugin comes with a set of pre-configured boilerplate descriptions to use out-of-the-box. Each boilerplate description has an autogenerated task associated with it with the name "boilerplate${BOILERPLATE_NAME}". These boilerplates are:

- **Web Service Resource**. Generates common java class files required for the different layers of abstraction that a typical (CRUD) web service resource requires: `Entity`, `QueryParameters`, `Repository`, `RepositoryCustom`, `RepositoryImpl`, `Controller`, `DTO`, `Resource` and `ResourceAssembler`. It also generates other utility classes, only if they are not already present (by having executed this task for another target web service resource). . Task configuration example:

```gradle
boilerplateWebServiceResource {
  taskDataModel = [
    srcDir: sourceSets.generated.java.srcDirs.first(), 
    packagePrefix: 'com.my.microservice.',
    resourceName: 'MyGeneratedResource'
  ]
}
```

- **Gradle Internal Object Plugin**. Generates the directories, packages, common skeleton classes (plugin, task and extension) and `build.gradle` file for a groovy based internal object plugin ($projectDir/buildSrc). Task configuration example:

```gradle
boilerplateGradlePlugin {
  taskDataModel = [
    pluginClassName: 'com.my.MyPlugin',
    extensionName: 'myPlugin'
  ]
}
```

- **Java Gradle Subproject**. Generates the gradle files, directories and hooks for a java based gradle subproject. Task configuration example:

```gradle
boilerplateJavaSubproject {
  taskDataModel = [
    subProjectName: 'swing',
    subProjectPrefix: 'module'
  ]
}
```

- **Javascript Gradle Subproject**. Generates the gradle files, directories and hooks for a javascript based gradle subproject. Task configuration example:

```gradle
boilerplateJavascriptSubproject {
  taskDataModel = [
    subProjectName: 'js',
    subProjectPrefix: 'module'
  ]
}
```

